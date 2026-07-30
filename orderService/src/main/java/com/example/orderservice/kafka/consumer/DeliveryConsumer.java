package com.example.orderservice.kafka.consumer;

import com.example.orderservice.entity.DeliveryStatus;
import com.example.orderservice.entity.OrderStatus;
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
            List<UUID> eventIds = records.stream().map(record -> UUID.fromString(record.key())).toList();
            orderService.updateDeliveryStatusBatch(eventIds, DeliveryStatus.STORE_ARRIVED);
            ack.acknowledge();

        } catch (Exception e) {
            log.error("delivery store arrived processing failed", e);
            throw e;
        }
    }

    @KafkaListener(topics = "delivery_started", concurrency = "3")
    public void deliveryStarted(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            List<UUID> eventIds = records.stream().map(record -> UUID.fromString(record.key())).toList();
            orderService.updateDeliveryStatusBatch(eventIds, DeliveryStatus.DELIVERING);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("delivery started processing failed", e);
            throw e;
        }
    }

    @KafkaListener(topics = "delivery_completed", concurrency = "3")
    public void deliveryCompleted(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            List<UUID> eventIds = records.stream().map(record -> UUID.fromString(record.key())).toList();
            orderService.updateDeliveryStatusBatch(eventIds, DeliveryStatus.COMPLETED);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("delivery completed processing failed", e);
            throw e;
        }
    }
}
