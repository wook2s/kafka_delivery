package com.example.deliveryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_accept")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AcceptOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

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

    public static AcceptOutbox createOutbox(Delivery delivery, String payload) {
        AcceptOutbox acceptOutbox = new AcceptOutbox();
        acceptOutbox.setOrderId(delivery.getId());
        acceptOutbox.setEventId(delivery.getEventId());
        acceptOutbox.setStatus(OutboxStatus.READY);
        acceptOutbox.setCreateId("STORE_SERVICE");
        acceptOutbox.setCreatedAt(LocalDateTime.now());
        acceptOutbox.setUpdateId("STORE_SERVICE");
        acceptOutbox.setUpdatedAt(LocalDateTime.now());
        acceptOutbox.setPayload(payload);
        return acceptOutbox;
    }

    public void publishComplete() {
        this.status = OutboxStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void publishFail() {
        this.status = OutboxStatus.FAILED;
    }
}
