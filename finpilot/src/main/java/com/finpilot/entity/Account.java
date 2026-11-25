package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false, length = 100)
    private String accountName;

    @Column(nullable = false, length = 30)
    private String accountType;

    @Column(nullable = false, length = 3)
    private String currency = "INR";

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(length = 50)
    private String plaidAccountId;

    @Column(length = 50)
    private String icon;

    @Column(length = 7)
    private String colour;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (currency == null) {
            currency = "INR";
        }

        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }

        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}