package com.example.deliveryservice.repository;

import com.example.deliveryservice.entity.Outbox;
import com.example.deliveryservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findTop100ByTopicAndStatusOrderByCreatedAtAsc(String topic, OutboxStatus outboxStatus);
}
