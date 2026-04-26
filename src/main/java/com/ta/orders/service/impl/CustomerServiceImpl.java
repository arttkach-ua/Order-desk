package com.ta.orders.service.impl;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.mappers.CustomerMapper;
import com.ta.orders.model.Customer;
import com.ta.orders.model.Expeditor;
import com.ta.orders.repository.CustomerRepository;
import com.ta.orders.repository.ExpeditorRepository;
import com.ta.orders.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ExpeditorRepository expeditorRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        log.debug("Saving customer: {}", customerDto);
        Customer customer = customerMapper.toEntity(customerDto);
        Customer saved = customerRepository.save(customer);
        return customerMapper.toDto(saved);
    }

    @Override
    public List<CustomerDto> getCustomers() {
        log.debug("Getting all customers");
        return customerRepository.findAll().stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getById(Long id) {
        log.debug("Getting customer by id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto update(Long id, CustomerDto customerDto) {
        log.debug("Updating customer with id: {}", id);
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existing.setName(customerDto.getName());
        existing.setEmail(customerDto.getEmail());
        existing.setPhone(customerDto.getPhone());

        if (customerDto.getExpeditorId() != null) {
            Expeditor expeditor = expeditorRepository.findById(customerDto.getExpeditorId())
                    .orElseThrow(() -> new RuntimeException("Expeditor not found with id: " + customerDto.getExpeditorId()));
            existing.setExpeditor(expeditor);
        } else {
            existing.setExpeditor(null);
        }

        Customer updated = customerRepository.save(existing);
        return customerMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting customer with id: {}", id);
        customerRepository.deleteById(id);
    }
}
