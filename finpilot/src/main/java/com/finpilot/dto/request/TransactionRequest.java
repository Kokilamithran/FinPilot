package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {

    @NotBlank
    private String transactionType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Transaction amount must be positive")
    private BigDecimal amount;

    private BigDecimal baseCurrencyAmount;

    @NotBlank
    private String currency;

    private BigDecimal exchangeRate;

    @NotNull
    private LocalDate transactionDate;

    private String merchant;

    private String notes;

    private String tags;

    private Boolean isRecurring;

    private String receiptUrl;

    private Boolean isAutoImported;

    private LocalDateTime importedAt;

    private Long userId;

    @NotNull
    private Long accountId;

    private Long categoryId;

    private Long recurringTransactionId;
}