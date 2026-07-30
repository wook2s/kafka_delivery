package com.example.orderservice.service;

import com.example.orderservice.dto.OrderItemRequestDto;
import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.entity.*;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OutboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OutboxRepository outboxRepository;

    @Test
    @DisplayName("주문 생성 시 Outbox가 READY 상태로 생성")
    void orderCreate() {
        OrderRequestDTO dto = createOrderRequest();

        Long id = orderService.createOrder(dto);

        Order order = orderRepository.findById(id).orElseThrow();

        assertThat(order.getUserId()).isEqualTo(1L);
        assertThat(order.getItems()).hasSize(2);
        assertThat(order.getEventId()).isNotNull();

        Outbox outbox = outboxRepository.findByEventId(order.getEventId());

        assertThat(order.getEventId()).isEqualTo(outbox.getEventId());
        assertThat(outbox.getStatus()).isEqualTo(OutboxStatus.READY);
    }

    @Test
    @DisplayName("주문상태 순차 변경")
    void updateOrderStatus() {
        OrderRequestDTO dto = createOrderRequest();

        Long id = orderService.createOrder(dto);
        Order order = orderRepository.findById(id).orElseThrow();

        orderService.updateOrderStatusBatch(List.of(order.getEventId()), OrderStatus.ACCEPTED);
        Order updated1 = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updated1.getStatus()).isEqualTo(OrderStatus.ACCEPTED);

        orderService.updateOrderStatusBatch(List.of(order.getEventId()), OrderStatus.PREPARED);
        Order updated2 = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updated2.getStatus()).isEqualTo(OrderStatus.PREPARED);
    }

    @Test
    @DisplayName("배달상태 순차 변경")
    void updateDeliveryStatus() {
        OrderRequestDTO dto = createOrderRequest();

        Long id = orderService.createOrder(dto);
        Order order = orderRepository.findById(id).orElseThrow();

        orderService.updateDeliveryStatusBatch(List.of(order.getEventId()), DeliveryStatus.STORE_ARRIVED);
        Order updated1 = orderRepository.findById(id).orElseThrow();
        assertThat(updated1.getDeliveryStatus()).isEqualTo(DeliveryStatus.STORE_ARRIVED);

        orderService.updateDeliveryStatusBatch(List.of(order.getEventId()), DeliveryStatus.DELIVERING);
        Order updated2 = orderRepository.findById(id).orElseThrow();
        assertThat(updated2.getDeliveryStatus()).isEqualTo(DeliveryStatus.DELIVERING);

        orderService.updateDeliveryStatusBatch(List.of(order.getEventId()), DeliveryStatus.COMPLETED);
        Order updated3 = orderRepository.findById(id).orElseThrow();
        assertThat(updated3.getDeliveryStatus()).isEqualTo(DeliveryStatus.COMPLETED);
    }

    private OrderRequestDTO createOrderRequest() {
        OrderItemRequestDto item1 = new OrderItemRequestDto();
        item1.setMenuName("chicken");
        item1.setQuantity(1);
        item1.setPrice(10000);

        OrderItemRequestDto item2 = new OrderItemRequestDto();
        item2.setMenuName("coke");
        item2.setQuantity(1);
        item2.setPrice(2000);

        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setUserId(1L);
        dto.setStoreId(1L);
        dto.setAddress("some where");
        dto.setItems(List.of(item1, item2));

        return dto;
    }
}
