package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.Outbox;
import com.example.orderservice.payload.OrderCreatedPayload;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long createOrder(OrderRequestDTO orderDTO) {
        Order order = Order.createOrder(orderDTO);
        Order savedOrder = orderRepository.save(order);

        OrderCreatedPayload payload = OrderCreatedPayload.from(order);
        String jsonData = objectMapper.writeValueAsString(payload);

        Outbox outbox = Outbox.createOutbox(savedOrder, "order-requested", jsonData);
        outboxRepository.save(outbox);

        return savedOrder.getId();
    }

    @Transactional
    public void orderAccepted(UUID eventId) {
        Order order = orderRepository.findByEventId(eventId);
        order.accepted();
    }

    @Transactional
    public void orderPrepared(UUID eventId) {
        orderRepository.findByEventId(eventId).prepared();
    }

    @Transactional
    public void deliveryStoreArrived(UUID eventId) {
        orderRepository.findByEventId(eventId).deliveryStoreArrived();
    }

    @Transactional
    public void deliveryStarted(UUID uuid) {
        orderRepository.findByEventId(uuid).deliveryStarted();
    }

    @Transactional
    public void deliveryCompleted(UUID uuid) {
        orderRepository.findByEventId(uuid).completed();
    }
}
