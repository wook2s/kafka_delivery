package com.example.storeservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String orderPayload;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String deliveryPayload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(nullable = false)
    private String createId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String updateId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Outbox createOutbox(Order order, String orderPayload, String deliveryPayload) {
        Outbox outbox = new Outbox();
        outbox.setOrderId(order.getId());
        outbox.setEventId(order.getEventId());
        outbox.setStatus(OutboxStatus.READY);
        outbox.setCreateId("STORE_SERVICE");
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setUpdateId("STORE_SERVICE");
        outbox.setUpdatedAt(LocalDateTime.now());

        outbox.setOrderPayload(orderPayload);
        outbox.setDeliveryPayload(deliveryPayload);
        return outbox;
    }

    public void publishComplete() {
        this.status = OutboxStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void publishFail() {
        this.status = OutboxStatus.FAILED;
    }
}
