package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.AcceptOutbox;
import com.example.deliveryservice.entity.OutboxStatus;
import com.example.deliveryservice.kafka.producer.DeliveryAcceptProducer;
import com.example.deliveryservice.repository.AcceptOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcceptOutboxService {

    private final DeliveryAcceptProducer deliveryAcceptProducer;
    private final AcceptOutboxRepository acceptOutboxRepository;

    @Transactional
    public void produceOutbox() {
        List<AcceptOutbox> acceptOutboxes = acceptOutboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
        for(AcceptOutbox acceptOutbox : acceptOutboxes) {
            try {
                deliveryAcceptProducer.produce(acceptOutbox.getEventId().toString(), acceptOutbox.getPayload());
                acceptOutbox.publishComplete();
            } catch (Exception  e) {
                acceptOutbox.publishFail();
            }
        }
    }
}
