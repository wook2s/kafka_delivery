package com.example.deliveryservice.scheduler;

import com.example.deliveryservice.service.AcceptOutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AcceptOutboxProduceScheduler {

    private final AcceptOutboxService acceptOutboxService;

    @Scheduled(fixedDelay = 1000)
    public void produceOutbox() {
        acceptOutboxService.produceOutbox();
    }
}
