package com.ta.orders.service;

import com.ta.orders.model.Customer;
import com.ta.orders.repository.CustomerRepository;
import com.ta.orders.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void shouldWriteIntoDBOnce() {
        Customer customer = new Customer(0L, "testName", null, "test@gmail.com", "1111");
        customerService.save(customer);
        verify(customerRepository, times(1)).save(customer);
    }
}