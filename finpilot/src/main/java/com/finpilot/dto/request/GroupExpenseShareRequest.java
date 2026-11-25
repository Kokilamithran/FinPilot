package com.finpilot.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GroupExpenseShareRequest {

    @NotNull
    private Long groupExpenseId;

    @NotNull
    private BigDecimal shareAmount;

    private Boolean isSettled;

    private LocalDateTime settledAt;

    private String settledMode;
}
