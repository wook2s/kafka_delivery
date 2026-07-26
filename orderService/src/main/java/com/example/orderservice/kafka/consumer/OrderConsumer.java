package com.example.orderservice.kafka.consumer;

import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;
    //private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order_accepted")
    public void orderAccepted(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String payload) {
        log.info("consume key : {}, Payload : {}", eventId, payload);
        orderService.orderAccepted(UUID.fromString(eventId));
    }

    @KafkaListener(topics = "order_prepared")
    public void orderPrepared(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String payload) {
        log.info("consume key : {}, Payload : {}", eventId, payload);
        orderService.orderPrepared(UUID.fromString(eventId));
    }
}
