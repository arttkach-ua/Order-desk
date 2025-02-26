package com.ta.orders.service;

import com.ta.orders.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer save(Customer customer);

    List<Customer> getCustomers();
}
