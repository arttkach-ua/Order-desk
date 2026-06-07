package com.ta.orders.service;

import com.ta.orders.dto.order.OrderRequestDto;
import com.ta.orders.dto.order.OrderResponseDto;

public interface OrderService {

    OrderResponseDto create(OrderRequestDto order);
}
