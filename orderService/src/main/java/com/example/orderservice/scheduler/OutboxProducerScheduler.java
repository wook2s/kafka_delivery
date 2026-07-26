package com.example.orderservice.scheduler;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.kafka.producer.OutboxProducer;
import com.example.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxProducerScheduler {

    private final OutboxService outboxService;
    private final OutboxProducer outboxProducer;

    @Scheduled(fixedDelay = 1000)
    public void publishEvent() {
        List<Outbox> outboxes = outboxService.getReadyOutboxes();

        for (Outbox outbox : outboxes) {
            outboxProducer.produce(outbox.getTopic(), outbox.getEventId().toString(), outbox.getPayload())
                    .orTimeout(5, TimeUnit.SECONDS)
                    .whenComplete((result, ex) -> {
                        if(ex == null) {
                            outboxService.publishComplete(outbox.getId());
                        } else {
                            if(ex instanceof TimeoutException) {
                                outboxService.publishTimeout(outbox.getId(), ex.getMessage());
                            } else {
                                outboxService.publishFail(outbox.getId(), ex.getMessage());
                            }
                        }
                    });
        }
    }
}
