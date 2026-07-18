package com.example.orderservice.entity;

import com.example.orderservice.dto.OrderRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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

    private Long userId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> items = new ArrayList<>();

    private String createId;
    private Timestamp createdAt;
    private String updateId;
    private Timestamp updatedAt;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public static Order createOrder(OrderRequestDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setStatus(OrderStatus.REQUESTED);

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
