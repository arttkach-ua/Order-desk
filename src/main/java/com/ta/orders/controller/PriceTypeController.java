package com.ta.orders.controller;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.service.PriceTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/price_type")
public class PriceTypeController {

    private final PriceTypeService priceTypeService;

    @GetMapping
    public List<PriceTypeDto> getAll() {
        return priceTypeService.getAll();
    }

     @PostMapping
     @ResponseStatus(HttpStatus.CREATED)
     public PriceTypeDto create(@RequestBody PriceTypeDto priceType) {
         return priceTypeService.create(priceType);
     }

     @PostMapping("/batch")
     @ResponseStatus(HttpStatus.CREATED)
     public List<PriceTypeDto> createBatch(@RequestBody List<PriceTypeDto> priceTypes) {
         return priceTypeService.create(priceTypes);
     }
}
