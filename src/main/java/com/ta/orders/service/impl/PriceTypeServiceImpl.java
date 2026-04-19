package com.ta.orders.service.impl;

import com.ta.orders.model.PriceType;
import com.ta.orders.repository.PriceTypeRepository;
import com.ta.orders.service.PriceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceTypeServiceImpl implements PriceTypeService {

    private final PriceTypeRepository priceTypeRepository;

    @Override
    @CacheEvict(value = "priceTypeDefault", allEntries = true)
    public PriceType create(PriceType priceType) {
        return priceTypeRepository.save(priceType);
    }

    @Override
    public List<PriceType> getAll() {
        return priceTypeRepository.findAll();
    }

    @Override
    public PriceType getClientsPriceTypeOrDefault(long clientId) {
        log.debug("Fetching price type for client ID: {}", clientId);

        return priceTypeRepository.findByClientId(clientId)
                .or(this::getDefaultPriceType)
                .orElseGet(() -> {
                    log.warn("Neither client-specific nor default price type found for client ID: {}", clientId);
                    return null;
                });
    }

    @Cacheable(value = "priceTypeDefault", unless = "#result == null")
    @Override
    public Optional<PriceType> getDefaultPriceType() {
        log.debug("Querying default price type from database");
        return priceTypeRepository.findByIsDefaultTrue();
    }
}
