package com.ta.orders.controller;

import com.ta.orders.dto.BatchPriceRequestDto;
import com.ta.orders.dto.BatchPriceValidationResult;
import com.ta.orders.dto.PriceDto;
import com.ta.orders.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Prices", description = "APIs for managing product prices")
public class PricesController {

    private final PriceService priceService;

    @PostMapping("/batch")
    @Operation(summary = "Register prices in batch", description = "Registers multiple prices for products in a single batch request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Prices registered successfully or validation failed",
                    content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {List.class, BatchPriceValidationResult.class}))),
            @ApiResponse(responseCode = "400", description = "Invalid batch price request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BatchPriceValidationResult.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> batchRegisterPrices(@RequestBody BatchPriceRequestDto request) {
        List<PriceDto> savedPrices = new ArrayList<>();
        BatchPriceValidationResult validationResult = priceService.batchSave(request, savedPrices);

        if (!validationResult.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationResult);
        }

        return ResponseEntity.ok(savedPrices);
    }

    @GetMapping
    @Operation(summary = "Get all prices", description = "Retrieves all prices in the system, including expired and current prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all prices retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PriceDto> getAllPrices() {
        return priceService.findAll();
    }

    @GetMapping("/current")
    @Operation(summary = "Get all current prices", description = "Retrieves only the current active prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of current prices retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PriceDto> getAllCurrentPrices() {
        return priceService.findAllCurrent();
    }

    @GetMapping("/current/{priceType}")
    @Operation(summary = "Get current prices by type", description = "Retrieves current prices filtered by a specific price type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of current prices for the type retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid price type"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PriceDto> getAllCurrentPricesByType(
            @Parameter(description = "Price type to filter by (e.g., RETAIL, WHOLESALE)", example = "RETAIL")
            @PathVariable String priceType) {
        return priceService.findAllCurrentByPriceType(priceType);
    }
}

