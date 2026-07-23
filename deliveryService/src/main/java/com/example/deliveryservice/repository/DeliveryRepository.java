package com.example.deliveryservice.repository;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.entity.DeliveryStatus;
import com.example.deliveryservice.entity.FoodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findTop30ByDeliveryStatusOrderByCreatedAtAsc(DeliveryStatus status);

    Delivery findByEventId(UUID eventId);

    List<Delivery> findTop30ByDeliveryStatusAndFoodStatus(DeliveryStatus deliveryStatus, FoodStatus foodStatus);
}
