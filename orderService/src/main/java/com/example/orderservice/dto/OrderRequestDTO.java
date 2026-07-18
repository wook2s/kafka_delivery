package com.example.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long userId;
    private Long storeId;
    private String address;
    private List<OrderItemRequestDto> items;
}
