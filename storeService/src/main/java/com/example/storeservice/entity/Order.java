package com.example.storeservice.entity;

import com.example.storeservice.payload.OrderAcceptPayload;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

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

    public static Order createOrderFromWaitingPayload(UUID eventId, OrderAcceptPayload payload) {
        Order order = new Order();
        order.setEventId(eventId);
        order.setUserId(payload.getUserId());
        order.setAddress(payload.getAddress());
        order.setStoreId(payload.getStoreId());
        order.setStatus(OrderStatus.REQUESTED);
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setCreateId("STORE_SERVICE");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdateId("STORE_SERVICE");
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
        this.status = OrderStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "STORE_SERVICE";
    }

    public void prepared() {
        this.status = OrderStatus.PREPARED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "STORE_SERVICE";
    }

    public void deliveryStoreArrived() {
        this.deliveryStatus = DeliveryStatus.STORE_ARRIVED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "STORE_SERVICE";
    }

    public void deliveryStarted() {
        this.deliveryStatus = DeliveryStatus.DELIVERING;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "STORE_SERVICE";
    }

    public void completed() {
        this.deliveryStatus = DeliveryStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "STORE_SERVICE";
    }
}
