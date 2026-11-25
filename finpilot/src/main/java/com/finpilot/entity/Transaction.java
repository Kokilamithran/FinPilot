package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false, length = 20)
    private String transactionType;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal amount;

    @Column(precision = 15, scale = 4)
    private BigDecimal baseCurrencyAmount;

    @Column(nullable = false, length = 3)
    private String currency = "INR";

    @Column(precision = 10, scale = 6)
    private BigDecimal exchangeRate;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(length = 200)
    private String merchant;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(columnDefinition = "TEXT")
    private String tags;

    @Column(nullable = false)
    private Boolean isRecurring = false;

    @Column(length = 500)
    private String receiptUrl;

    @Column(nullable = false)
    private Boolean isAutoImported = false;

    private LocalDateTime importedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "recurring_id")
    private RecurringTransaction recurringTransaction;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (currency == null) {
            currency = "INR";
        }

        if (isRecurring == null) {
            isRecurring = false;
        }

        if (isAutoImported == null) {
            isAutoImported = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();

        if (currency == null) {
            currency = "INR";
        }

        if (isRecurring == null) {
            isRecurring = false;
        }

        if (isAutoImported == null) {
            isAutoImported = false;
        }
    }
}