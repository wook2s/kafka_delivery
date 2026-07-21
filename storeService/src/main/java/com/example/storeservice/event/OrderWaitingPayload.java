package com.example.storeservice.event;

import com.example.storeservice.entity.Order;
import com.example.storeservice.entity.OrderItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderWaitingPayload {
    private Long orderId;
    private Long userId;
    private Long storeId;
    private String address;
    private List<OrderItem> items;

    public static OrderWaitingPayload from(Order order) {
        OrderWaitingPayload payload = new OrderWaitingPayload();
        payload.setOrderId(order.getId());
        payload.setUserId(order.getUserId());
        payload.setStoreId(order.getStoreId());
        payload.setItems(order.getItems());
        payload.setAddress(order.getAddress());

        return payload;
    }
}
