package com.example.orderservice.kafka.producer;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.entity.OutboxStatus;
import com.example.orderservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProducerScheduler {

    private final OutboxRepository outboxRepository;
    private final OutboxProducer outboxProducer;

    @Scheduled(fixedDelay = 1000)
    public void publishEvent(){
        log.info("scheduled !");
        List<Outbox> outboxes = outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
        log.info("size : {}", outboxes.size());
    }
}
