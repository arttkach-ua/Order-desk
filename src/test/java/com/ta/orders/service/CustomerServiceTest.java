package com.ta.orders.service;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.mappers.CustomerMapper;
import com.ta.orders.model.Customer;
import com.ta.orders.repository.CustomerRepository;
import com.ta.orders.repository.ExpeditorRepository;
import com.ta.orders.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ExpeditorRepository expeditorRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldWriteIntoDBOnce() {
        CustomerDto customerDto = new CustomerDto(null, "testName", "test@gmail.com", "1111", null, null);
        Customer customer = new Customer(0L, "testName", null, "test@gmail.com", "1111", null);
        Customer savedCustomer = new Customer(1L, "testName", null, "test@gmail.com", "1111", null);

        when(customerMapper.toEntity(customerDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(new CustomerDto(1L, "testName", "test@gmail.com", "1111", null, null));

        customerService.save(customerDto);

        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toEntity(customerDto);
        verify(customerMapper, times(1)).toDto(savedCustomer);
    }
}