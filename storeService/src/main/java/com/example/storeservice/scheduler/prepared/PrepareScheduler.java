package com.example.storeservice.scheduler.prepared;

import com.example.storeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrepareScheduler {

    private final OrderService orderService;

    @Scheduled(fixedDelay = 3000)
    public void prepareOrders() {
        orderService.prepareOrders();
    }
}
