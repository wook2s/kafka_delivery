package com.example.deliveryservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryAcceptProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String DELIVERY_ACCEPT_TOPIC = "delivery-accepted";

    public void produce(String key, String message) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(DELIVERY_ACCEPT_TOPIC, key, message).get();
    }
}
