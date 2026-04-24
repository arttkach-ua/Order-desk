package com.ta.orders.service.impl;

import com.ta.orders.dto.BatchPriceRequestDto;
import com.ta.orders.dto.BatchPriceValidationResult;
import com.ta.orders.dto.PriceDto;
import com.ta.orders.dto.PriceItemDto;
import com.ta.orders.mappers.PriceMapper;
import com.ta.orders.model.Price;
import com.ta.orders.repository.PriceRepository;
import com.ta.orders.service.PriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    @Override
    @Transactional
    @CacheEvict(value = "productCategories", allEntries = true)
    public BatchPriceValidationResult batchSave(BatchPriceRequestDto request, List<PriceDto> savedPrices) {
        BatchPriceValidationResult validationResult = new BatchPriceValidationResult();

        // Validate request
        if (request.getValidFrom() == null) {
            validationResult.setMissingValidFrom(true);
            validationResult.setInvalid("Valid from timestamp is required");
            return validationResult;
        }

        if (request.getPriceType() == null || request.getPriceType().isBlank()) {
            validationResult.setMissingPriceType(true);
            validationResult.setInvalid("Price type is required");
            return validationResult;
        }

        if (request.getPrices() == null || request.getPrices().isEmpty()) {
            validationResult.setEmptyPricesList(true);
            validationResult.setInvalid("Prices list cannot be empty");
            return validationResult;
        }

        // Validate products exist
        List<Long> requestedProductIds = request.getPrices().stream()
                .map(PriceItemDto::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<Long> existingProductIds = priceRepository.findExistingProductIds(requestedProductIds);

        List<Long> invalidProductIds = requestedProductIds.stream()
                .filter(id -> !existingProductIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidProductIds.isEmpty()) {
            validationResult.setInvalidProductIds(invalidProductIds);
            validationResult.setInvalid("Invalid product IDs: " + invalidProductIds);
            return validationResult;
        }

        // Process each price item
        for (PriceItemDto priceItem : request.getPrices()) {
            // Close current price if exists
            Optional<Price> currentPrice = priceRepository.findByProductIdAndPriceTypeAndIsCurrent(
                    priceItem.getProductId(),
                    request.getPriceType(),
                    true
            );

            if (currentPrice.isPresent()) {
                // Set valid_to on existing current price
                priceRepository.closeCurrentPrice(
                        priceItem.getProductId(),
                        request.getPriceType(),
                        request.getValidFrom()
                );
                log.debug("Closed current price for product {} with price type {} at {}",
                        priceItem.getProductId(), request.getPriceType(), request.getValidFrom());
            }

            // Create new current price
            Price newPrice = new Price();
            newPrice.setProductId(priceItem.getProductId());
            newPrice.setPriceType(request.getPriceType());
            newPrice.setPrice(priceItem.getPrice());
            newPrice.setValidFrom(request.getValidFrom());
            newPrice.setValidTo(null);  // NULL means current
            newPrice.setCurrent(true);

            Price savedPrice = priceRepository.save(newPrice);
            savedPrices.add(priceMapper.toDto(savedPrice));

            log.debug("Created new current price for product {} with price type {} from {}",
                    priceItem.getProductId(), request.getPriceType(), request.getValidFrom());
        }

        log.info("Successfully saved {} prices for price type {} from {}",
                savedPrices.size(), request.getPriceType(), request.getValidFrom());

        return validationResult;
    }

    @Override
    public List<PriceDto> findAll() {
        log.debug("Fetching all price history");
        return priceRepository.findAll()
                .stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PriceDto> findAllCurrent() {
        log.debug("Fetching all current prices");
        return priceRepository.findByIsCurrent(true)
                .stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PriceDto> findAllCurrentByPriceType(String priceType) {
        log.debug("Fetching all current prices for price type {}", priceType);
        return priceRepository.findAllCurrentByPriceType(priceType)
                .stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());
    }
}

