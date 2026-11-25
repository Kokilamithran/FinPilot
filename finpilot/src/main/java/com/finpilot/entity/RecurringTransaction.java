package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recurring_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecurringTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recurringId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 20)
    private String transactionType;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 200)
    private String merchant;

    @Column(nullable = false, length = 20)
    private String frequency;

    @Column(nullable = false)
    private LocalDate nextDueDate;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean autoLog = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (isActive == null) {
            isActive = true;
        }

        if (autoLog == null) {
            autoLog = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}