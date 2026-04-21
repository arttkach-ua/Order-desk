package com.ta.orders.controller;

import com.ta.orders.dto.BatchPriceRequestDto;
import com.ta.orders.dto.BatchPriceValidationResult;
import com.ta.orders.dto.PriceDto;
import com.ta.orders.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prices")
@CrossOrigin
@RequiredArgsConstructor
public class PricesController {

    private final PriceService priceService;

    @PostMapping("/batch")
    public ResponseEntity<?> batchRegisterPrices(@RequestBody BatchPriceRequestDto request) {
        List<PriceDto> savedPrices = new ArrayList<>();
        BatchPriceValidationResult validationResult = priceService.batchSave(request, savedPrices);

        if (!validationResult.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }

        return ResponseEntity.ok(savedPrices);
    }

    @GetMapping
    public List<PriceDto> getAllPrices() {
        return priceService.findAll();
    }

    @GetMapping("/current")
    public List<PriceDto> getAllCurrentPrices() {
        return priceService.findAllCurrent();
    }

    @GetMapping("/current/{priceType}")
    public List<PriceDto> getAllCurrentPricesByType(@PathVariable String priceType) {
        return priceService.findAllCurrentByPriceType(priceType);
    }
}

