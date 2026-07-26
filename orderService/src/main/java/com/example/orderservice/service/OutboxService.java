package com.example.orderservice.service;

import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.entity.Outbox;
import com.example.orderservice.entity.OutboxStatus;
import com.example.orderservice.repository.OutboxRepository;
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
    private static final int MAX_RETRY_CNT = 2;

    @Transactional(readOnly = true)
    public List<Outbox> getReadyOutboxes() {
        return outboxRepository.findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus.READY);
    }

    @Transactional
    public void publishComplete(Long id) {
        Outbox outbox = outboxRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Outbox not found : " + id));
        outbox.publishComplete();
        outbox.increaseTryCnt();
    }

    @Transactional
    public void publishTimeout(Long id, String message) {
        Outbox outbox = outboxRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Outbox not found : " + id));
        outbox.publishTimeout();
        outbox.increaseTryCnt();
    }

    @Transactional
    public void publishFail(Long id, String message) {
        Outbox outbox = outboxRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Outbox not found : " + id));
        outbox.publishFail();
        outbox.increaseTryCnt();
    }

    @Transactional(readOnly = true)
    public List<Outbox> getTimeoutOutboxes() {
        return outboxRepository.findTop100ByStatusAndTryCntLessThanEqualOrderByCreatedAtAsc(OutboxStatus.TIMEOUT, MAX_RETRY_CNT);
    }
}
