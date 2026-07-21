package com.example.storeservice.kafka.consumer;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.OrderStatus;
import com.example.storeservice.entity.Outbox;
import com.example.storeservice.event.OrderWaitingPayload;
import com.example.storeservice.repository.OrderRepository;
import com.example.storeservice.repository.OutboxRepository;
import com.example.storeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "order-requested", groupId = "store-waiting")
    public void consumeOrder(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        log.info("consume key : {}, json : {}", eventId, json);

        OrderWaitingPayload payload = objectMapper.readValue(json, OrderWaitingPayload.class);
        Order order = Order.createOrderFromWaitingPayload(UUID.fromString(eventId), payload);
        orderService.saveOrder(order);
    }

    @Scheduled(fixedDelay = 3000)
    public void acceptOrders() {
        orderService.acceptOrders();
    }
}
