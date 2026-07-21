package com.example.orderservice.entity;

import com.example.orderservice.dto.OrderRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    @ToString.Exclude
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
        order.setEventId(UUID.randomUUID());
        order.setUserId(dto.getUserId());
        order.setAddress(dto.getAddress());
        order.setStoreId(dto.getStoreId());
        order.setStatus(OrderStatus.REQUESTED);
        order.setCreateId("ORDER_SERVICE");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdateId("ORDER_SERVICE");
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

    public void accepted() {
        this.status = OrderStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "ORDER_SERVICE";
    }
}
