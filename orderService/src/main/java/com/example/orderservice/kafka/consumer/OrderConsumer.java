package com.example.orderservice.kafka.consumer;

import com.example.orderservice.entity.Order;
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
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {

    private final OrderService orderService;

    @KafkaListener(topics = "order_accepted", concurrency = "3")
    public void orderAccepted(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            List<UUID> eventIds = records.stream().map(record -> UUID.fromString(record.key())).toList();
            orderService.updateOrderStatusBatch(eventIds, OrderStatus.ACCEPTED);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("order accepted processing failed", e);
            throw e;
        }
    }

    @KafkaListener(topics = "order_prepared", concurrency = "3")
    public void orderPrepared(List<ConsumerRecord<String, String>> records, Acknowledgment ack) {
        try {
            List<UUID> eventIds = records.stream().map(record -> UUID.fromString(record.key())).toList();
            orderService.updateOrderStatusBatch(eventIds, OrderStatus.PREPARED);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("order prepared processing failed", e);
            throw e;
        }
    }
}
