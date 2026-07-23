package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.Outbox;
import com.example.deliveryservice.entity.OutboxStatus;
import com.example.deliveryservice.kafka.producer.KafkaProducer;
import com.example.deliveryservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final KafkaProducer kafkaProducer;

    @Transactional
    public void acceptProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("delivery_accepted",OutboxStatus.READY);
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
    public void arrivalProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("delivery_store_arrived",OutboxStatus.READY);
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
    public void deliveryStartProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("delivery_started",OutboxStatus.READY);
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
    public void completeProduce() {
        List<Outbox> outboxes = outboxRepository.findTop100ByTopicAndStatusOrderByCreatedAtAsc("delivery_completed",OutboxStatus.READY);
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
