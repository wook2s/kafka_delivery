package com.example.orderservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    private String menuName;
    private int quantity;
    private int price;
}
