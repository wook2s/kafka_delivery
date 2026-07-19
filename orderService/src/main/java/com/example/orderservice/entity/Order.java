package com.example.orderservice.entity;

import com.example.orderservice.dto.OrderRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    @Column(nullable = false)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private String createId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String updateId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public static Order createOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setStoreId(dto.getStoreId());
        order.setStatus(OrderStatus.REQUESTED);
        order.setCreateId("ORDER_API");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdateId("ORDER_API");
        order.setUpdatedAt(LocalDateTime.now());

        dto.getItems().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuName(item.getMenuName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            order.addItem(orderItem);
        });
        return order;
    }
}
