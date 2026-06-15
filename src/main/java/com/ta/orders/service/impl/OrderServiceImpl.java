package com.ta.orders.service.impl;

import com.ta.orders.dto.documents.order.OrderListResponseDto;
import com.ta.orders.dto.documents.order.OrderRequestDto;
import com.ta.orders.dto.documents.order.OrderResponseDto;
import com.ta.orders.mappers.documents.OrderMapper;
import com.ta.orders.model.documents.order.Order;
import com.ta.orders.repository.OrderRepository;
import com.ta.orders.service.NumberingService;
import com.ta.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final NumberingService numberingService;

    @Override
    public OrderResponseDto create(OrderRequestDto order) {
        Order toSave = orderMapper.toEntity(order);
        toSave.setNumber(numberingService.calculateNewNumber(orderRepository));
        Order saved = orderRepository.save(toSave);

        return orderMapper.toDto(saved);
    }

    @Override
    public List<OrderListResponseDto> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toListDto)
                .toList();
    }
}
