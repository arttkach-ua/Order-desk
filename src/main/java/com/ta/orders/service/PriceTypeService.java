package com.ta.orders.service;

import com.ta.orders.model.PriceType;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface PriceTypeService {

    PriceType create(PriceType priceType);

    List<PriceType> getAll();

    PriceType getClientsPriceTypeOrDefault(long ClientId);


    @Cacheable(value = "priceTypeDefault", unless = "#result == null")
    Optional<PriceType> getDefaultPriceType();
}
