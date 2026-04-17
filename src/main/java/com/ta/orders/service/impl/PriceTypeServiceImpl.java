package com.ta.orders.service.impl;

import com.ta.orders.model.PriceType;
import com.ta.orders.repository.PriceTypeRepository;
import com.ta.orders.service.PriceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceTypeServiceImpl implements PriceTypeService {

    private final PriceTypeRepository priceTypeRepository;

    @Override
    public PriceType create(PriceType priceType) {
        return priceTypeRepository.save(priceType);
    }

    @Override
    public List<PriceType> getAll() {
        return priceTypeRepository.findAll();
    }

    @Override
    public PriceType getClientsPriceTypeOrDefault(long ClientId) {
        //TODO TBD
        return null;
    }
}
