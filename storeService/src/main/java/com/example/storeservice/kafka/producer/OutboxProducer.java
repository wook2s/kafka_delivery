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
    private final static String ORDER_ACCEPT_TOPIC = "order-accepted";
    private final static String DELIVERY_REQUEST_TOPIC = "delivery-requested";

    public void produce(String key, String message) throws ExecutionException, InterruptedException {
        kafkaTemplate.send(ORDER_ACCEPT_TOPIC, key, message).get();
    }

    public void produceAcceptAndDelivery(String key, String orderPayload, String deliveryPayload) throws ExecutionException, InterruptedException {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send(ORDER_ACCEPT_TOPIC, key, orderPayload);
            operations.send(DELIVERY_REQUEST_TOPIC, key, deliveryPayload);
            return true;
        });
    }
}
