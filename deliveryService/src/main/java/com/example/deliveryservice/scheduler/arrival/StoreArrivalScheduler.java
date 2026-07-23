package com.example.deliveryservice.scheduler.arrival;

import com.example.deliveryservice.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StoreArrivalScheduler {

    private final DeliveryService deliveryService;

    @Scheduled(fixedDelay = 3000)
    public void arrivedStore() {
        deliveryService.arrivedStore();
    }
}
