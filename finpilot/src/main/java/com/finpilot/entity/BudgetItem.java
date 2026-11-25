package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedAmount;

    @Column(precision = 12, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer alertPercentage = 80;

    @Column(nullable = false)
    private Boolean rolloverEnabled = false;

    @Column(precision = 12, scale = 2)
    private BigDecimal rolloverAmount = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {

        if (spentAmount == null) {
            spentAmount = BigDecimal.ZERO;
        }

        if (alertPercentage == null) {
            alertPercentage = 80;
        }

        if (rolloverEnabled == null) {
            rolloverEnabled = false;
        }

        if (rolloverAmount == null) {
            rolloverAmount = BigDecimal.ZERO;
        }
    }
}