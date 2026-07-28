package com.example.orderservice.scheduler;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.kafka.producer.OutboxProducer;
import com.example.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {

    private final OutboxService outboxService;
    private final OutboxProducer outboxProducer;
    private final ThreadPoolTaskExecutor kafkaProducerCallbackExecutor;

    @Scheduled(fixedDelay = 1000)
    public void retryTimeout() {
        List<Outbox> outboxes = outboxService.getTimeoutOutboxes();

        for (Outbox outbox : outboxes) {
            outboxProducer.produce(outbox)
                    .orTimeout(5, TimeUnit.SECONDS)
                    .whenComplete((result, ex) -> {
                        kafkaProducerCallbackExecutor.execute(() -> handleCallback(outbox, ex));

                    });
        }
    }

    private void handleCallback(Outbox outbox, Throwable ex) {
        if (ex == null) {
            outboxService.publishComplete(outbox.getEventId());
        } else if (ex instanceof TimeoutException) {
            outboxService.publishTimeout(outbox.getEventId(), ex.getMessage());
        } else {
            outboxService.publishFail(outbox.getEventId(), ex.getMessage());
        }
    }
}
