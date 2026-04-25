package com.ta.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Result of batch price validation")
public class BatchPriceValidationResult {
    @Schema(description = "Whether the batch price request is valid", example = "true")
    private boolean valid;
    @Schema(description = "List of product IDs that failed validation", example = "[1, 3, 5]")
    private List<Long> invalidProductIds = new ArrayList<>();
    @Schema(description = "Whether the price type is invalid", example = "false")
    private boolean invalidPriceType;
    @Schema(description = "Whether the validFrom date is missing", example = "false")
    private boolean missingValidFrom;
    @Schema(description = "Whether the price type is missing", example = "false")
    private boolean missingPriceType;
    @Schema(description = "Whether the prices list is empty", example = "false")
    private boolean emptyPricesList;
    @Schema(description = "Error message describing validation failure", example = "Price type not found")
    private String errorMessage;

    public BatchPriceValidationResult() {
        this.valid = true;
    }

    public void setInvalid(String message) {
        this.valid = false;
        this.errorMessage = message;
    }
}



