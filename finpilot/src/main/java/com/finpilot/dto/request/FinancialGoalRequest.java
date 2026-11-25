package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialGoalRequest {

    @NotBlank
    private String goalName;

    @NotBlank
    private String goalCategory;

    @NotNull
    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    @NotNull
    private LocalDate targetDate;

    private String priority;

    private String status;

    private Long userId;

    private Long linkedAccountId;

    private String abandonedReason;
}