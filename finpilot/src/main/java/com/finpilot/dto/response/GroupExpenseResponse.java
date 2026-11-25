package com.finpilot.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GroupExpenseResponse {

    private Long groupExpenseId;

    private Long groupId;

    private Long transactionId;

    private BigDecimal totalAmount;

    private String splitType;

    private Long createdBy;

    private LocalDateTime createdDate;
}
