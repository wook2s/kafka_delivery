package com.example.deliveryservice.scheduler.complete;

import com.example.deliveryservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompleteProduceScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 1000)
    public void completeProduce() {
        outboxService.completeProduce();
    }
}
