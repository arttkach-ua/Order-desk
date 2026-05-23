package com.ta.orders.service;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.dto.ExpeditorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExpeditorService {

    ExpeditorDto create(ExpeditorDto expeditorDto);

    Page<ExpeditorDto> getAll(Pageable pageable);

    ExpeditorDto getById(Long id);

    ExpeditorDto update(Long id, ExpeditorDto expeditorDto);

    void delete(Long id);

    List<CustomerDto> getCustomersByExpeditorId(Long expeditorId);
}

