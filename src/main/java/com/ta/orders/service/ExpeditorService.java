package com.ta.orders.service;

import com.ta.orders.dto.CustomerDto;
import com.ta.orders.dto.ExpeditorDto;

import java.util.List;

public interface ExpeditorService {

    ExpeditorDto create(ExpeditorDto expeditorDto);

    List<ExpeditorDto> getAll();

    ExpeditorDto getById(Long id);

    ExpeditorDto update(Long id, ExpeditorDto expeditorDto);

    void delete(Long id);

    List<CustomerDto> getCustomersByExpeditorId(Long expeditorId);
}

