package com.example.storeservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String TOPIC = "order-accepted";

    public void produce(String key, String message) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(TOPIC, key, message).get();
    }
}
