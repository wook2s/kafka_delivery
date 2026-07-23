package com.example.deliveryservice.entity;

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
@ToString
public class Outbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private String topic;

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

    public static Outbox createOutbox(Delivery delivery, String topic,String payload) {
        Outbox outbox = new Outbox();
        outbox.setOrderId(delivery.getId());
        outbox.setEventId(delivery.getEventId());
        outbox.setTopic(topic);
        outbox.setStatus(OutboxStatus.READY);
        outbox.setCreateId("STORE_SERVICE");
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setUpdateId("STORE_SERVICE");
        outbox.setUpdatedAt(LocalDateTime.now());
        outbox.setPayload(payload);
        return outbox;
    }

    public void publishComplete() {
        this.status = OutboxStatus.PUBLISHED;
        this.updateId = "STORE_SERVICE";
        this.updatedAt = LocalDateTime.now();
    }

    public void publishFail() {
        this.status = OutboxStatus.FAILED;
    }
}
