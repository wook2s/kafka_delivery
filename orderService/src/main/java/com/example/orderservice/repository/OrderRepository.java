package com.example.orderservice.repository;

import com.example.orderservice.entity.DeliveryStatus;
import com.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByEventId(UUID eventId);

}
