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
import java.util.UUID;

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
    public void publishComplete(UUID eventId) {
        int updated = outboxRepository.updateStatusAndTryCntByEventId(eventId, OutboxStatus.PUBLISHED);
        if (updated == 0) {
            throw new IllegalArgumentException("outbox not found : " + eventId);
        }
    }

    @Transactional
    public void publishTimeout(UUID eventId, String errorMsg) {
        int updated = outboxRepository.updateStatusAndTryCntAndErrorMsgByEventId(eventId, OutboxStatus.TIMEOUT, errorMsg);
        if (updated == 0) {
            throw new IllegalArgumentException("outbox not found : " + eventId);
        }
    }

    @Transactional
    public void publishFail(UUID eventId, String errorMsg) {
        int updated = outboxRepository.updateStatusAndTryCntAndErrorMsgByEventId(eventId, OutboxStatus.FAILED, errorMsg);
        if (updated == 0) {
            throw new IllegalArgumentException("outbox not found : " + eventId);
        }
    }

    @Transactional(readOnly = true)
    public List<Outbox> getTimeoutOutboxes() {
        return outboxRepository.findTop100ByStatusAndTryCntLessThanEqualOrderByCreatedAtAsc(OutboxStatus.TIMEOUT, MAX_RETRY_CNT);
    }
}
