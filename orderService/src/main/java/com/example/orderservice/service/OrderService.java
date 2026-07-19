package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.Outbox;
import com.example.orderservice.event.OrderCreatedPayload;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
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

        Outbox outbox = Outbox.createOutbox(savedOrder, jsonData);
        outboxRepository.save(outbox);

        return savedOrder.getId();
    }
}
