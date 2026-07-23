package com.example.storeservice.scheduler.accept;

import com.example.storeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AcceptScheduler {

    private final OrderService orderService;

    @Scheduled(fixedDelay = 3000)
    public void acceptOrders() {
        orderService.acceptOrders();
    }
}
