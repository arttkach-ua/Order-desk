package com.ta.orders.service.impl;

import com.ta.orders.dto.ReferenceDto;
import com.ta.orders.dto.order.OrderRequestDto;
import com.ta.orders.dto.order.OrderResponseDto;
import com.ta.orders.mappers.documents.OrderMapper;
import com.ta.orders.model.documents.order.Order;
import com.ta.orders.repository.OrderRepository;
import com.ta.orders.service.NumberingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private NumberingService numberingService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Test
    void shouldCreateOrderAndReturnDto_givenValidRequest() {
        // Given
        OrderRequestDto request = new OrderRequestDto(1L, Instant.now(), 2L, "notes", BigDecimal.TEN, Collections.emptyList());
        Order toSave = Order.builder().date(request.date()).comments(request.comments()).sum(request.sum()).build();
        Order saved = Order.builder().id(1L).date(request.date()).number(5).comments(request.comments()).sum(request.sum()).build();
        OrderResponseDto expectedDto = new OrderResponseDto(1L, request.date(), 5, new ReferenceDto(1L, "C"), new ReferenceDto(2L, "P"), request.sum(), request.comments(), Collections.emptyList());

        given(orderMapper.toEntity(request)).willReturn(toSave);
        given(numberingService.calculateNewNumber(orderRepository)).willReturn(5);
        given(orderRepository.save(toSave)).willReturn(saved);
        given(orderMapper.toDto(saved)).willReturn(expectedDto);

        // When
        OrderResponseDto result = orderServiceImpl.create(request);

        // Then
        verify(numberingService, times(1)).calculateNewNumber(orderRepository);
        verify(orderRepository, times(1)).save(toSave);
        verify(orderMapper, times(1)).toEntity(request);
        verify(orderMapper, times(1)).toDto(saved);
        assertThat(toSave.getNumber()).isEqualTo(5);
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    void shouldSetNullNumber_whenNumberingServiceReturnsNull() {
        // Given
        OrderRequestDto request = new OrderRequestDto(1L, Instant.now(), 2L, "notes", BigDecimal.ZERO, Collections.emptyList());
        Order toSave = Order.builder().date(request.date()).comments(request.comments()).sum(request.sum()).build();
        Order saved = Order.builder().id(2L).date(request.date()).comments(request.comments()).sum(request.sum()).build();
        OrderResponseDto expectedDto = new OrderResponseDto(2L, request.date(), null, new ReferenceDto(1L, "C"), new ReferenceDto(2L, "P"), request.sum(), request.comments(), Collections.emptyList());

        given(orderMapper.toEntity(request)).willReturn(toSave);
        given(numberingService.calculateNewNumber(orderRepository)).willReturn(null);
        given(orderRepository.save(toSave)).willReturn(saved);
        given(orderMapper.toDto(saved)).willReturn(expectedDto);

        // When
        OrderResponseDto result = orderServiceImpl.create(request);

        // Then
        verify(numberingService, times(1)).calculateNewNumber(orderRepository);
        verify(orderRepository, times(1)).save(toSave);
        assertThat(toSave.getNumber()).isNull();
        assertThat(result).isEqualTo(expectedDto);
    }
}