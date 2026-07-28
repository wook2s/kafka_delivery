package com.example.orderservice.kafka.consumer;

import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class DeliveryConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "delivery_store_arrived", concurrency = "3")
    public void deliveryStoreArrived(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                orderService.deliveryStoreArrived(UUID.fromString(record.key()));
            }
            ack.acknowledge();

        } catch (Exception e) {
            throw e;
        }
    }

    @KafkaListener(topics = "delivery_started", concurrency = "3")
    public void deliveryStarted(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                orderService.deliveryStarted(UUID.fromString(record.key()));
            }
            ack.acknowledge();
        } catch (Exception e) {
            throw e;
        }
    }

    @KafkaListener(topics = "delivery_completed", concurrency = "3")
    public void deliveryCompleted(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            for (ConsumerRecord<String, String> record : records) {
                UUID eventId = UUID.fromString(record.key());
                orderService.deliveryCompleted(eventId);
            }
            ack.acknowledge();
        } catch (Exception e) {
            throw e;
        }
    }
}
