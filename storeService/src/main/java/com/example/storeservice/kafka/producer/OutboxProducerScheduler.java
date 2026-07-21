package com.example.storeservice.kafka.producer;

import com.example.storeservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxProducerScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 1000)
    public void produceOutbox() {
        outboxService.produceOutbox();
    }
}
