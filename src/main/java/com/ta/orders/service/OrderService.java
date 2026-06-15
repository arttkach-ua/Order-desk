package com.ta.orders.service;

import com.ta.orders.dto.documents.order.OrderListResponseDto;
import com.ta.orders.dto.documents.order.OrderRequestDto;
import com.ta.orders.dto.documents.order.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto create(OrderRequestDto order);

    List<OrderListResponseDto> findAll();
}
