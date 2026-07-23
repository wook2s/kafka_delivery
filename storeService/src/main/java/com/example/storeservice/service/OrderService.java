package com.example.storeservice.service;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.OrderStatus;
import com.example.storeservice.entity.Outbox;
import com.example.storeservice.payload.OrderAcceptPayload;
import com.example.storeservice.repository.OrderRepository;
import com.example.storeservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional
    public void acceptOrders() {
        List<Order> orders = orderRepository.findTop30ByStatusOrderByCreatedAtAsc(OrderStatus.REQUESTED);

        List<Outbox> outboxes = orders.stream().map(order -> {
            OrderAcceptPayload payload = OrderAcceptPayload.from(order);

            return Outbox.createOutbox(order, "order_accepted", objectMapper.writeValueAsString(payload));
        }).toList();

        for (Order order : orders) {
            order.accepted();
        }
        outboxRepository.saveAll(outboxes);
    }

    @Transactional
    public void prepareOrders() {
        List<Order> orders = orderRepository.findTop30ByStatusOrderByCreatedAtAsc(OrderStatus.ACCEPTED);

        List<Outbox> outboxes = orders.stream().map(order -> {
            OrderAcceptPayload payload = OrderAcceptPayload.from(order);

            return Outbox.createOutbox(order, "order_prepared", objectMapper.writeValueAsString(payload));
        }).toList();

        for (Order order : orders) {
            order.prepared();
        }
        outboxRepository.saveAll(outboxes);
    }

    @Transactional
    public void deliveryStoreArrived(UUID uuid) {
        orderRepository.findByEventId(uuid).deliveryStoreArrived();
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
