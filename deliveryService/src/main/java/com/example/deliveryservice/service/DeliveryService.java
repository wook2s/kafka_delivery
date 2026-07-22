package com.example.deliveryservice.service;

import com.example.deliveryservice.entity.CompleteOutbox;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.entity.DeliveryStatus;
import com.example.deliveryservice.entity.AcceptOutbox;
import com.example.deliveryservice.payload.DeliveryRequestPayload;
import com.example.deliveryservice.repository.CompleteOutboxRepository;
import com.example.deliveryservice.repository.DeliveryRepository;
import com.example.deliveryservice.repository.AcceptOutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AcceptOutboxRepository acceptOutboxRepository;
    private final CompleteOutboxRepository completeOutboxRepository;
    private final ObjectMapper objectMapper;

    public void saveDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    @Transactional
    public void acceptDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByStatusOrderByCreatedAtAsc(DeliveryStatus.REQUESTED);

        List<AcceptOutbox> acceptOutboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);

            return AcceptOutbox.createOutbox(delivery, objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.accepted();
        }
        acceptOutboxRepository.saveAll(acceptOutboxes);
    }

    @Transactional
    public void completeDeliveries() {
        List<Delivery> deliveries = deliveryRepository.findTop30ByStatusOrderByCreatedAtAsc(DeliveryStatus.ACCEPTED);

        List<CompleteOutbox> outboxes = deliveries.stream().map(delivery -> {
            DeliveryRequestPayload deliveryPayload = DeliveryRequestPayload.from(delivery);
            return CompleteOutbox.createOutbox(delivery, objectMapper.writeValueAsString(deliveryPayload));
        }).toList();

        for (Delivery delivery : deliveries) {
            delivery.completed();
        }
        completeOutboxRepository.saveAll(outboxes);
    }
}
