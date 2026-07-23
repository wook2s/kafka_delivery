package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.entity.DeliveryStatus;
import com.example.deliveryservice.entity.FoodStatus;
import com.example.deliveryservice.entity.Outbox;
import com.example.deliveryservice.payload.DeliveryRequestPayload;
import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void acceptDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByDeliveryStatusOrderByCreatedAtAsc(DeliveryStatus.REQUESTED);

        List<Outbox> outboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);

            return Outbox.createOutbox(delivery, "delivery_accepted",objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.accepted();
        }
        outboxRepository.saveAll(outboxes);
    }

    @Transactional
    public void arrivedStore() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByDeliveryStatusOrderByCreatedAtAsc(DeliveryStatus.ACCEPTED);

        List<Outbox> outboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);
            return Outbox.createOutbox(delivery, "delivery_store_arrived",objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.storeArrived();
        }
        outboxRepository.saveAll(outboxes);
    }

    @Transactional
    public void makeFoodPrepared(UUID eventId) {
        deliveryRepository.findByEventId(eventId).setFoodStatus(FoodStatus.PREPARED);
    }

    @Transactional
    public void deliveryStart() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByDeliveryStatusAndFoodStatus(DeliveryStatus.STORE_ARRIVED, FoodStatus.PREPARED);

        List<Outbox> outboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);
            return Outbox.createOutbox(delivery, "delivery_started",objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.deliveryStart();
        }
        outboxRepository.saveAll(outboxes);
    }

    @Transactional
    public void complete() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByDeliveryStatusOrderByCreatedAtAsc(DeliveryStatus.DELIVERING);

        List<Outbox> outboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);
            return Outbox.createOutbox(delivery, "delivery_completed",objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.completed();
        }
        outboxRepository.saveAll(outboxes);
    }
}
