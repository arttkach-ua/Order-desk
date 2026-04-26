package com.ta.orders.service.impl;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.dto.ExpeditorDto;
import com.ta.orders.mappers.CustomerMapper;
import com.ta.orders.mappers.ExpeditorMapper;
import com.ta.orders.model.Expeditor;
import com.ta.orders.repository.CustomerRepository;
import com.ta.orders.repository.ExpeditorRepository;
import com.ta.orders.service.ExpeditorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpeditorServiceImpl implements ExpeditorService {

    private final ExpeditorRepository expeditorRepository;
    private final CustomerRepository customerRepository;
    private final ExpeditorMapper expeditorMapper;
    private final CustomerMapper customerMapper;

    @Override
    public ExpeditorDto create(ExpeditorDto expeditorDto) {
        log.debug("Creating expeditor: {}", expeditorDto);
        Expeditor expeditor = expeditorMapper.toEntity(expeditorDto);
        Expeditor saved = expeditorRepository.save(expeditor);
        return expeditorMapper.toDto(saved);
    }

    @Override
    public List<ExpeditorDto> getAll() {
        log.debug("Getting all expeditors");
        return expeditorRepository.findAll().stream()
                .map(expeditorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpeditorDto getById(Long id) {
        log.debug("Getting expeditor by id: {}", id);
        Expeditor expeditor = expeditorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expeditor not found with id: " + id));
        return expeditorMapper.toDto(expeditor);
    }

    @Override
    public ExpeditorDto update(Long id, ExpeditorDto expeditorDto) {
        log.debug("Updating expeditor with id: {}", id);
        Expeditor existing = expeditorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expeditor not found with id: " + id));

        existing.setName(expeditorDto.getName());
        existing.setPhone(expeditorDto.getPhone());

        Expeditor updated = expeditorRepository.save(existing);
        return expeditorMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting expeditor with id: {}", id);
        expeditorRepository.deleteById(id);
    }

    @Override
    public List<CustomerDto> getCustomersByExpeditorId(Long expeditorId) {
        log.debug("Getting customers by expeditor id: {}", expeditorId);
        // Verify expeditor exists
        expeditorRepository.findById(expeditorId)
                .orElseThrow(() -> new RuntimeException("Expeditor not found with id: " + expeditorId));

        return customerRepository.findByExpeditorId(expeditorId).stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }
}

