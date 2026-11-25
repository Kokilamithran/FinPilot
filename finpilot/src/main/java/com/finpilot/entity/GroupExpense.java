package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupExpenseId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToOne
    @JoinColumn(name = "txn_id", nullable = false, unique = true)
    private Transaction transaction;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 20)
    private String splitType;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}