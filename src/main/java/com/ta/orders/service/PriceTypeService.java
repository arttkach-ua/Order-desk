package com.ta.orders.service;

import com.ta.orders.model.PriceType;

import java.util.List;

public interface PriceTypeService {

    PriceType create(PriceType priceType);

    List<PriceType> getAll();

    PriceType getClientsPriceTypeOrDefault(long ClientId);
}
