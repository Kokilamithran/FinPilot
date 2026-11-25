package com.finpilot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalContributionRequest {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDate contributionDate;

    private String notes;

    @NotNull
    private Long goalId;
}