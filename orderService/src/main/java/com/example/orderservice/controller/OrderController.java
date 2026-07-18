package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDTO;
import com.example.orderservice.dto.ResponseDTO;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseDTO<Long> order(@RequestBody OrderRequestDTO orderDTO) {
        log.info("ORDER ARRIVED : {}", orderDTO);
        Long orderId = orderService.createOrder(orderDTO);

        return new ResponseDTO<Long>(HttpStatus.OK.value(), orderId);
    }
}
