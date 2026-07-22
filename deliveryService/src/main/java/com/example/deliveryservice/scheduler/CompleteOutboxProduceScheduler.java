package com.example.deliveryservice.scheduler;

import com.example.deliveryservice.service.AcceptOutboxService;
import com.example.deliveryservice.service.CompleteOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompleteOutboxProduceScheduler {

    private final CompleteOutboxService completeOutboxService;

    @Scheduled(fixedDelay = 1000)
    public void produceOutbox() {
        completeOutboxService.produceOutbox();
    }
}
