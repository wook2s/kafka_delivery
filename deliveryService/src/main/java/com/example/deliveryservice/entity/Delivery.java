package com.example.deliveryservice.entity;

import com.example.deliveryservice.payload.DeliveryRequestPayload;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Delivery {
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
    private DeliveryStatus status;

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

    public static Delivery createDeliveryFromPendingPayload(UUID eventId, DeliveryRequestPayload payload) {
        Delivery order = new Delivery();
        order.setEventId(eventId);
        order.setUserId(payload.getUserId());
        order.setAddress(payload.getAddress());
        order.setStoreId(payload.getStoreId());
        order.setStatus(DeliveryStatus.REQUESTED);
        order.setCreateId("DELIVERY_SERVICE");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdateId("DELIVERY_SERVICE");
        order.setUpdatedAt(LocalDateTime.now());

        payload.getItems().forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setMenuName(item.getMenuName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            order.addItem(orderItem);
        });
        return order;
    }

    public void accepted() {
        this.status = DeliveryStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "DELIVERY_SERVICE";
    }

    public void completed() {
        this.status = DeliveryStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "DELIVERY_SERVICE";
    }
}
