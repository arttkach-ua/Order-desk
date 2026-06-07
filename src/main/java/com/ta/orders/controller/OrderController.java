package com.ta.orders.controller;

import com.ta.orders.dto.order.OrderRequestDto;
import com.ta.orders.dto.order.OrderResponseDto;
import com.ta.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    OrderResponseDto create(@RequestBody OrderRequestDto order) {
        return orderService.create(order);
    }
}
