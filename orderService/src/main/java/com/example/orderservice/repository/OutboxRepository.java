package com.example.orderservice.repository;

import com.example.orderservice.entity.Outbox;
import com.example.orderservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus status);

}
