package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {

    private Long accountId;

    private String accountName;

    private String accountType;

    private String currency;

    private BigDecimal currentBalance;

    private BigDecimal creditLimit;

    private String plaidAccountId;

    private String icon;

    private String colour;

    private Boolean isActive;

    private Long userId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}