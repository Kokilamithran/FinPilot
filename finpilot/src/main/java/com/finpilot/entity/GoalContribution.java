package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "goal_contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contributionId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate contributionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "goal_id", nullable = false)
    private FinancialGoal financialGoal;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}