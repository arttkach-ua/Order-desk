package com.ta.orders.service.impl;

import com.ta.orders.dto.order.OrderRequestDto;
import com.ta.orders.dto.order.OrderResponseDto;
import com.ta.orders.mappers.documents.OrderMapper;
import com.ta.orders.model.documents.order.Order;
import com.ta.orders.repository.OrderRepository;
import com.ta.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto create(OrderRequestDto order) {
        Order saved = orderRepository.save(orderMapper.toEntity(order));

        return orderMapper.toDto(saved);
    }
}
