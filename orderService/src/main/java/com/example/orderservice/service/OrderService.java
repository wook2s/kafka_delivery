package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.entity.DeliveryStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
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

        Outbox outbox = Outbox.createOutbox(savedOrder, "order_requested", jsonData);
        outboxRepository.save(outbox);

        return savedOrder.getId();
    }

    @Transactional
    public void orderAccepted(UUID eventId) {
//        orderRepository.findByEventId(eventId).accepted();
        int cnt = orderRepository.updateStatusByEventId(eventId, OrderStatus.ACCEPTED);
        if(cnt == 0) {
            throw new IllegalArgumentException("order not found : " + eventId.toString());
        }
    }

    @Transactional
    public void orderPrepared(UUID eventId) {
//        orderRepository.findByEventId(eventId).prepared();
        int cnt = orderRepository.updateStatusByEventId(eventId, OrderStatus.PREPARED);
        if(cnt == 0) {
            throw new IllegalArgumentException("order not found : " + eventId.toString());
        }
    }

    @Transactional
    public void deliveryStoreArrived(UUID eventId) {
//        orderRepository.findByEventId(eventId).deliveryStoreArrived();
        int cnt = orderRepository.updateDeliveryStatusByEventId(eventId, DeliveryStatus.STORE_ARRIVED);
        if(cnt == 0) {
            throw new IllegalArgumentException("order not found : " + eventId.toString());
        }
    }

    @Transactional
    public void deliveryStarted(UUID eventId) {
//        orderRepository.findByEventId(uuid).deliveryStarted();
        int cnt = orderRepository.updateDeliveryStatusByEventId(eventId, DeliveryStatus.DELIVERING);
        if(cnt == 0) {
            throw new IllegalArgumentException("order not found : " + eventId.toString());
        }
    }

    @Transactional
    public void deliveryCompleted(UUID eventId) {
//        orderRepository.findByEventId(eventId).completed();
        int cnt = orderRepository.updateDeliveryStatusByEventId(eventId, DeliveryStatus.COMPLETED);
        if(cnt == 0) {
            throw new IllegalArgumentException("order not found : " + eventId.toString());
        }
    }
}
