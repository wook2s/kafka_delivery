package com.example.deliveryservice.scheduler.complete;

import com.example.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompleteScheduler {

    private final DeliveryService deliveryService;

    @Scheduled(fixedDelay = 3000)
    public void complete() {
        deliveryService.complete();
    }
}
