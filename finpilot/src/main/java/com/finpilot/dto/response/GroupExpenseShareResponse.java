package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GroupExpenseShareResponse {

    private Long shareId;

    private Long groupExpenseId;

    private Long userId;

    private BigDecimal shareAmount;

    private Boolean isSettled;

    private LocalDateTime settledAt;

    private String settledMode;
}
