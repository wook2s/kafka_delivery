package com.example.deliveryservice.kafka.consumer;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.payload.DeliveryRequestPayload;
import com.example.deliveryservice.service.DeliveryService;
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
public class DeliveryRequestConsumer {

    private final ObjectMapper objectMapper;
    private final DeliveryService deliveryService;

    @KafkaListener(topics = "delivery-requested", groupId = "delivery-waiting")
    public void consumeDelivery(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        log.info("consume key : {}, json : {}", eventId, json);

        DeliveryRequestPayload payload = objectMapper.readValue(json, DeliveryRequestPayload.class);
        Delivery delivery = Delivery.createDeliveryFromPendingPayload(UUID.fromString(eventId), payload);
        deliveryService.saveDelivery(delivery);
    }
}
