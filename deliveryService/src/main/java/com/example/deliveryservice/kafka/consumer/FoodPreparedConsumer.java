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
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FoodPreparedConsumer {

    private final DeliveryService deliveryService;

    @KafkaListener(topics = "order_prepared")
    public void consumeDelivery(@Header(KafkaHeaders.RECEIVED_KEY) String eventId, @Payload String json) {
        log.info("prepared food consume key : {}, json : {}", eventId, json);
        deliveryService.makeFoodPrepared(UUID.fromString(eventId));
    }
}
