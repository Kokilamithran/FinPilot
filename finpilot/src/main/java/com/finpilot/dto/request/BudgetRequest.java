package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BudgetRequest {

    @NotBlank
    private String budgetName;

    @NotBlank
    private String periodType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    private String currency;

    private Boolean isActive;

    private Long userId;
}