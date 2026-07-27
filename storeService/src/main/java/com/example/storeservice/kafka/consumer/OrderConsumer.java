package com.example.storeservice.kafka.consumer;

import com.example.storeservice.entity.Order;
import com.example.storeservice.payload.OrderAcceptPayload;
import com.example.storeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @KafkaListener(topics = "order_requested")
    public void orderRequested(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        log.info("consume key : {}, json : {}", eventId, json);

        OrderAcceptPayload payload = objectMapper.readValue(json, OrderAcceptPayload.class);
        Order order = Order.createOrderFromWaitingPayload(UUID.fromString(eventId), payload);
        orderService.saveOrder(order);
    }
}
