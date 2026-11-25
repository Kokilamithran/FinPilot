package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long transactionId;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal baseCurrencyAmount;

    private String currency;

    private BigDecimal exchangeRate;

    private LocalDate transactionDate;

    private String merchant;

    private String notes;

    private String tags;

    private Boolean isRecurring;

    private String receiptUrl;

    private Boolean isAutoImported;

    private LocalDateTime importedAt;

    private Long userId;

    private Long accountId;

    private Long categoryId;

    private Long recurringTransactionId;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}