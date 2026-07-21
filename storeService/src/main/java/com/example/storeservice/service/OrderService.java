package com.example.storeservice.service;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.OrderStatus;
import com.example.storeservice.entity.Outbox;
import com.example.storeservice.event.OrderWaitingPayload;
import com.example.storeservice.repository.OrderRepository;
import com.example.storeservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void acceptOrders() {
        List<Order> orders = orderRepository.findTop30ByStatusOrderByCreatedAtAsc(OrderStatus.REQUESTED);

        List<Outbox> outboxes = orders.stream().map(order -> {
            OrderWaitingPayload payload = OrderWaitingPayload.from(order);
            return Outbox.createOutbox(order, objectMapper.writeValueAsString(payload));
        }).toList();

        for (Order order : orders) {
            order.accepted();
        }
        outboxRepository.saveAll(outboxes);
    }
}
