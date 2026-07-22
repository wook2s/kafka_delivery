package com.example.deliveryservice.payload;

import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.entity.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryRequestPayload {
    private Long orderId;
    private Long userId;
    private Long storeId;
    private String address;
    private List<OrderItem> items;

    public static DeliveryRequestPayload from(Delivery delivery) {
        DeliveryRequestPayload payload = new DeliveryRequestPayload();
        payload.setOrderId(delivery.getId());
        payload.setUserId(delivery.getUserId());
        payload.setStoreId(delivery.getStoreId());
        payload.setItems(delivery.getItems());
        payload.setAddress(delivery.getAddress());

        return payload;
    }
}
