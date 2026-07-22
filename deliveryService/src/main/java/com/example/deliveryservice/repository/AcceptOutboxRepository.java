package com.example.deliveryservice.repository;

import com.example.deliveryservice.entity.AcceptOutbox;
import com.example.deliveryservice.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcceptOutboxRepository extends JpaRepository<AcceptOutbox, Long> {

    List<AcceptOutbox> findTop100ByStatusOrderByCreatedAtAsc(OutboxStatus outboxStatus);
}
