package com.example.storeservice.scheduler.accept;

import com.example.storeservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AcceptProduceScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 1000)
    public void produceOutbox() {
        outboxService.acceptProduce();
    }
}
