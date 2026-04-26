package com.ta.orders.service;

import com.ta.orders.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto save(CustomerDto customerDto);

    List<CustomerDto> getCustomers();

    CustomerDto getById(Long id);

    CustomerDto update(Long id, CustomerDto customerDto);

    void delete(Long id);
}
