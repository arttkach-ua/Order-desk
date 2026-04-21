package com.ta.orders.service;

import com.ta.orders.dto.BatchPriceRequestDto;
import com.ta.orders.dto.BatchPriceValidationResult;
import com.ta.orders.dto.PriceDto;

import java.util.List;

public interface PriceService {

    BatchPriceValidationResult batchSave(BatchPriceRequestDto request, List<PriceDto> savedPrices);

    List<PriceDto> findAll();

    List<PriceDto> findAllCurrent();

    List<PriceDto> findAllCurrentByPriceType(String priceType);
}

