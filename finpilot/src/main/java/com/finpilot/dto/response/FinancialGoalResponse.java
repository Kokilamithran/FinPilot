package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FinancialGoalResponse {

    private Long goalId;

    private String goalName;

    private String goalCategory;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private LocalDate targetDate;

    private String priority;

    private String status;

    private Long userId;

    private Long linkedAccountId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private LocalDateTime completedAt;

    private String abandonedReason;
}