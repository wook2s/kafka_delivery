package com.example.storeservice.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produce(String topic, String key, String message) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(topic, key, message).get();
    }

    public void produceAcceptAndDelivery(String topic1, String topic2, String key1, String key2, String payload1, String payload2) throws ExecutionException, InterruptedException {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(topic1, key1, payload1);
            operations.send(topic2, key2, payload2);
            return true;
        });
    }
}
