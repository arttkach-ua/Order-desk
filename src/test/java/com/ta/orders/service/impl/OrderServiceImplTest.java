package com.ta.orders.service.impl;

import com.ta.orders.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

//    @Mock
//    private OrderRepository orderRepository;
//    @InjectMocks
//    private OrderServiceImpl orderServiceImpl;
//
//    @Test
//    public void shouldCalculateNewNumberWhenOldIsNull() {
//        when(orderRepository.findMaxNumber()).thenReturn(null);
//        String number = orderServiceImpl.calculateNewNumber();
//        assertEquals("1", number);
//    }
}