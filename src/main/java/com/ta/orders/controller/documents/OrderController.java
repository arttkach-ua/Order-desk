package com.ta.orders.controller.documents;

import com.ta.orders.dto.documents.order.OrderListResponseDto;
import com.ta.orders.dto.documents.order.OrderRequestDto;
import com.ta.orders.dto.documents.order.OrderResponseDto;
import com.ta.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    List<OrderListResponseDto> getAll() {
        return orderService.findAll();
    }
}
