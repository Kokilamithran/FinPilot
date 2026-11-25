package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupExpenseRequest {

    @NotNull
    private Long groupId;

    @NotNull
    private Long transactionId;

    @NotNull
    private BigDecimal totalAmount;

    @NotBlank
    private String splitType;
}
