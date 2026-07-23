package com.example.deliveryservice.scheduler.delivery;

import com.example.deliveryservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryStartProduceScheduler {

    private final OutboxService outboxService;

    @Scheduled(fixedDelay = 1000)
    public void deliveryStartProduce() {
        outboxService.deliveryStartProduce();
    }
}
