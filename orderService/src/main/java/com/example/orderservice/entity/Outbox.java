package com.example.orderservice.entity;

import com.example.orderservice.event.OrderCreatedPayload;
import jakarta.persistence.*;
import lombok.*;
import tools.jackson.databind.ObjectMapper;

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

    public static Outbox createOutbox(Order order, String payload) {
        Outbox outbox = new Outbox();
        outbox.setOrderId(order.getId());
        outbox.setEventId(UUID.randomUUID());
        outbox.setStatus(OutboxStatus.READY);
        outbox.setCreateId("ORDER-API");
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setUpdateId("ORDER-API");
        outbox.setUpdatedAt(LocalDateTime.now());

        outbox.setPayload(payload);

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
