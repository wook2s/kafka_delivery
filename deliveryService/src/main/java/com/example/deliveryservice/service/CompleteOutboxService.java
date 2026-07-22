package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.AcceptOutbox;
import com.example.deliveryservice.entity.CompleteOutbox;
import com.example.deliveryservice.entity.OutboxStatus;
import com.example.deliveryservice.kafka.producer.DeliveryAcceptProducer;
import com.example.deliveryservice.kafka.producer.DeliveryCompleteProducer;
import com.example.deliveryservice.repository.AcceptOutboxRepository;
import com.example.deliveryservice.repository.CompleteOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompleteOutboxService {

    private final DeliveryCompleteProducer deliveryCompleteProducer;
    private final CompleteOutboxRepository completeOutboxRepository;

    @Transactional
    public void produceOutbox() {
        List<CompleteOutbox> acceptOutboxes = completeOutboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
        for(CompleteOutbox outbox : acceptOutboxes) {
            try {
                deliveryCompleteProducer.produce(outbox.getEventId().toString(), outbox.getPayload());
                outbox.publishComplete();
            } catch (Exception  e) {
                outbox.publishFail();
            }
        }
    }
}
