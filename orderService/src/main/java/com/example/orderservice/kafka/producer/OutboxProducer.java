package com.example.orderservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String TOPIC = "order-topic";

    public void sendKafkaEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
