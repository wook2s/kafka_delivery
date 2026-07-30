package com.example.orderservice.repository;

import com.example.orderservice.entity.DeliveryStatus;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByEventId(UUID eventId);

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query("""
        update Order o
        set o.status = :status
        where o.eventId = :eventId
    """)
    int updateStatusByEventId(
            @Param("eventId") UUID eventId,
            @Param("status") OrderStatus status
    );

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true
    )
    @Query("""
        update Order o
        set o.deliveryStatus = :deliveryStatus
        where o.eventId = :eventId
    """)
    int updateDeliveryStatusByEventId(
            @Param("eventId") UUID eventId,
            @Param("deliveryStatus") DeliveryStatus deliveryStatus
    );

    @Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
    @Query("""
        update Order o
        set o.status = :status
        where o.eventId in :eventIds 
            and o.status <> :status
    """)
    int updateStatusByEventIds(
            @Param("eventIds") List<UUID> eventIds,
            @Param("status") OrderStatus status
    );

    @Modifying(
        clearAutomatically = true,
        flushAutomatically = true
    )
    @Query("""
        update Order o
        set o.deliveryStatus = :deliveryStatus
        where o.eventId in :eventIds
            and o.deliveryStatus <> :deliveryStatus
    """)
    int updateDeliveryStatusByEventIds(
            @Param("eventIds") List<UUID> eventIds,
            @Param("deliveryStatus") DeliveryStatus deliveryStatus
    );
}
