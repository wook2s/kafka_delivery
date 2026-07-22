package com.example.deliveryservice.repository;

import com.example.deliveryservice.entity.AcceptOutbox;
import com.example.deliveryservice.entity.CompleteOutbox;
import com.example.deliveryservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompleteOutboxRepository extends JpaRepository<CompleteOutbox, Long> {
    List<CompleteOutbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);
}
