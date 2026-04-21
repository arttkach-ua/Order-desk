package com.ta.orders.service;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.model.PriceType;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface PriceTypeService {

    PriceTypeDto create(PriceTypeDto priceType);

    List<PriceTypeDto> create(List<PriceTypeDto> priceTypes);

    List<PriceTypeDto> getAll();

    PriceType getClientsPriceTypeOrDefault(long ClientId);


    @Cacheable(value = "priceTypeDefault", unless = "#result == null")
    Optional<PriceType> getDefaultPriceType();
}
