package com.example.orderservice.kafka.producer;

import com.example.orderservice.entity.Outbox;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class OutboxProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CompletableFuture<SendResult<String, String>> produce(String topic, String key, String message) {
        return kafkaTemplate.send(topic, key, message);
    }

    public CompletableFuture<SendResult<String, String>> produce(Outbox outbox) {
        return kafkaTemplate.send(outbox.getTopic(), outbox.getEventId().toString(), outbox.getPayload());
    }
}
