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
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "delivery_store_arrived")
    public void deliveryStoreArrived(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        orderService.deliveryStoreArrived(UUID.fromString(eventId));
    }

    @KafkaListener(topics = "delivery_started")
    public void deliveryStarted(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        orderService.deliveryStarted(UUID.fromString(eventId));
    }

    @KafkaListener(topics = "delivery_completed")
    public void deliveryCompleted(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        orderService.deliveryCompleted(UUID.fromString(eventId));
    }
}
