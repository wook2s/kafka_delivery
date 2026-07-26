package com.example.orderservice.entity;

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

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(nullable = false)
    private int tryCnt = 0;

    @Column
    private String errorMsg;

    @Column(nullable = false)
    private String createId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String updateId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static Outbox createOutbox(Order order, String topic, String payload) {
        Outbox outbox = new Outbox();

        outbox.setOrderId(order.getId());
        outbox.setEventId(order.getEventId());
        outbox.setStatus(OutboxStatus.READY);

        outbox.setTopic(topic);
        outbox.setPayload(payload);

        outbox.setCreateId("ORDER_SERVICE");
        outbox.setCreatedAt(LocalDateTime.now());
        outbox.setUpdateId("ORDER_SERVICE");
        outbox.setUpdatedAt(LocalDateTime.now());

        return outbox;
    }

    public void publishComplete() {
        this.status = OutboxStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "ORDER_SERVICE";
    }

    public void publishFail() {
        this.status = OutboxStatus.FAILED;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "ORDER_SERVICE";
    }

    public void publishTimeout() {
        this.status = OutboxStatus.TIMEOUT;
        this.updatedAt = LocalDateTime.now();
        this.updateId = "ORDER_SERVICE";
    }

    public void increaseTryCnt() {
        this.tryCnt ++;
    }
}
