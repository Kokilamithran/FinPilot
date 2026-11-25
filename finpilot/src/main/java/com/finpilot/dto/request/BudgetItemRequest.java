package com.finpilot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetItemRequest {

    @NotNull
    private Long budgetId;

    @NotNull
    private Long categoryId;

    @NotNull
    private BigDecimal allocatedAmount;

    private BigDecimal spentAmount;

    private Integer alertPercentage;

    private Boolean rolloverEnabled;

    private BigDecimal rolloverAmount;

    private String notes;
}