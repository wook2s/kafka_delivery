package com.example.orderservice.repository;

import com.example.orderservice.entity.DeliveryStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByEventId(UUID eventId);

    @Modifying
    @Query("""
        update Order o
        set o.status = :status
        where o.eventId = :eventId
    """)
    int updateStatusByEventId(
            @Param("eventId") UUID eventId,
            @Param("status") OrderStatus status
    );

    @Modifying
    @Query("""
        update Order o
        set o.deliveryStatus = :deliveryStatus
        where o.eventId = :eventId
    """)
    int updateDeliveryStatusByEventId(
            @Param("eventId") UUID eventId,
            @Param("deliveryStatus") DeliveryStatus deliveryStatus
    );
}
