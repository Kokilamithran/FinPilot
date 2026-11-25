package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class GoalContributionResponse {

    private Long contributionId;

    private BigDecimal amount;

    private LocalDate contributionDate;

    private String notes;

    private Long goalId;

    private LocalDateTime createdDate;
}