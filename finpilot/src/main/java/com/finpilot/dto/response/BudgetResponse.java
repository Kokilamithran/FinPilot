package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BudgetResponse {

    private Long budgetId;

    private String budgetName;

    private String periodType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalAmount;

    private String currency;

    private Boolean isActive;

    private Long userId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}