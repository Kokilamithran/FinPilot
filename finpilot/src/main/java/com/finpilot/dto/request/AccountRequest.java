package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotBlank(message = "Account type is required")
    private String accountType;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Current balance is required")
    private BigDecimal currentBalance;

    private BigDecimal creditLimit;

    private String plaidAccountId;

    private String icon;

    private String colour;

    private Boolean isActive;

    private Long userId;
}