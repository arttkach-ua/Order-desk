package com.ta.orders.controller;

import com.ta.orders.dto.PriceTypeDto;
import com.ta.orders.service.PriceTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Price Types", description = "APIs for managing price types")
public class PriceTypeController {

    private final PriceTypeService priceTypeService;

    @GetMapping
    @Operation(summary = "Get all price types", description = "Retrieves a list of all available price types")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of price types retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceTypeDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PriceTypeDto> getAll() {
        return priceTypeService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new price type", description = "Creates a new price type with the provided name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Price type created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceTypeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public PriceTypeDto create(@RequestBody PriceTypeDto priceType) {
        return priceTypeService.create(priceType);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create multiple price types", description = "Creates multiple price types in a single batch request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Price types created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PriceTypeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<PriceTypeDto> createBatch(@RequestBody List<PriceTypeDto> priceTypes) {
        return priceTypeService.create(priceTypes);
    }
}


