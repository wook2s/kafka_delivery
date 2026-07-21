package com.example.orderservice.kafka.producer;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.entity.OutboxStatus;
import com.example.orderservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProducerScheduler {

    private final OutboxRepository outboxRepository;
    private final OutboxProducer outboxProducer;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void publishEvent(){
        List<Outbox> outboxes = outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
        if(outboxes.isEmpty()) {
            return;
        }
        for(Outbox outbox : outboxes) {
            try {
                outboxProducer.produce(outbox.getEventId().toString(), outbox.getPayload());
                outbox.publishComplete();
            } catch (Exception  e) {
                outbox.publishFail();
            }
        }
    }
}
