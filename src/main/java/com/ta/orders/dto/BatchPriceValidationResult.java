package com.ta.orders.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchPriceValidationResult {
    private boolean valid;
    private List<Long> invalidProductIds = new ArrayList<>();
    private boolean invalidPriceType;
    private boolean missingValidFrom;
    private boolean missingPriceType;
    private boolean emptyPricesList;
    private String errorMessage;

    public BatchPriceValidationResult() {
        this.valid = true;
    }

    public void setInvalid(String message) {
        this.valid = false;
        this.errorMessage = message;
    }
}

