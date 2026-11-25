package com.finpilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecurringTransactionRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String transactionType;

    @NotNull
    private BigDecimal amount;

    private String merchant;

    @NotBlank
    private String frequency;

    @NotNull
    private LocalDate nextDueDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    private Boolean autoLog;

    private Long userId;

    @NotNull
    private Long accountId;

    @NotNull
    private Long categoryId;
}