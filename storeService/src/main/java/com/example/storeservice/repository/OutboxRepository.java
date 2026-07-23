package com.example.storeservice.repository;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.Outbox;
import com.example.storeservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findTop100ByTopicAndStatusOrderByCreatedAtAsc(String topic, OutboxStatus status);
}
