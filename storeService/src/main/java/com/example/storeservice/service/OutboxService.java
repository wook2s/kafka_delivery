package com.example.storeservice.service;

import com.example.storeservice.entity.Outbox;
import com.example.storeservice.entity.OutboxStatus;
import com.example.storeservice.kafka.producer.KafkaProducer;
import com.example.storeservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {

    private final KafkaProducer kafkaProducer;
    private final OutboxRepository outboxRepository;

    @Transactional
    public void acceptProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("order_accepted",OutboxStatus.READY);
        for(Outbox outbox : outboxes) {
            try {
                kafkaProducer.produce(outbox.getTopic(), outbox.getEventId().toString(), outbox.getPayload());
                outbox.publishComplete();
            } catch (Exception  e) {
                outbox.publishFail();
            }
        }
    }

    @Transactional
    public void prepareProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("order_prepared",OutboxStatus.READY);
        for(Outbox outbox : outboxes) {
            try {
                kafkaProducer.produce(outbox.getTopic(), outbox.getEventId().toString(), outbox.getPayload());
                outbox.publishComplete();
            } catch (Exception  e) {
                outbox.publishFail();
            }
        }
    }
}
