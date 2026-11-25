package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RecurringTransactionResponse {

    private Long recurringId;
    private String name;
    private String transactionType;
    private BigDecimal amount;
    private String merchant;
    private String frequency;
    private LocalDate nextDueDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Boolean autoLog;
    private Long userId;
    private Long accountId;
    private Long categoryId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}