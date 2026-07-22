package com.example.storeservice.service;

import com.example.storeservice.entity.Outbox;
import com.example.storeservice.entity.OutboxStatus;
import com.example.storeservice.kafka.producer.OutboxProducer;
import com.example.storeservice.repository.OrderRepository;
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

    private final OutboxProducer outboxProducer;
    private final OutboxRepository outboxRepository;

    @Transactional("transactionManager")
    public void produceOutbox() {
        List<Outbox> outboxes = outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
        for(Outbox outbox : outboxes) {
            try {
//                outboxProducer.produce(outbox.getEventId().toString(), outbox.getPayload());
                outboxProducer.produceAcceptAndDelivery(outbox.getEventId().toString(), outbox.getOrderPayload(), outbox.getDeliveryPayload());
                outbox.publishComplete();
            } catch (Exception  e) {
                outbox.publishFail();
            }
        }
    }
}
