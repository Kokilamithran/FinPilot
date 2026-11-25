package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetItemResponse {

    private Long itemId;

    private Long budgetId;

    private Long categoryId;

    private BigDecimal allocatedAmount;

    private BigDecimal spentAmount;

    private Integer alertPercentage;

    private Boolean rolloverEnabled;

    private BigDecimal rolloverAmount;

    private String notes;
}