package com.example.orderservice.event;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedPayload {
    private Long orderId;
    private Long userId;
    private Long storeId;
    private List<OrderItem> items;

    public static OrderCreatedPayload from(Order order) {
        OrderCreatedPayload payload = new OrderCreatedPayload();
        payload.setOrderId(order.getId());
        payload.setUserId(order.getUserId());
        payload.setStoreId(order.getStoreId());
        payload.setItems(order.getItems());

        return payload;
    }
}
