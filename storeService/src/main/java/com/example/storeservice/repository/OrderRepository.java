package com.example.storeservice.repository;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findTop30ByStatusOrderByCreatedAtAsc(OrderStatus status);
}
