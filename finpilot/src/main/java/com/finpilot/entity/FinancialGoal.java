package com.finpilot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "financial_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    @Column(nullable = false, length = 200)
    private String goalName;

    @Column(nullable = false, length = 30)
    private String goalCategory;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate targetDate;

    @Column(length = 20)
    private String priority;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "linked_account_id")
    private Account linkedAccount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String abandonedReason;

    @OneToMany(mappedBy = "financialGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<GoalContribution> contributions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();

        if (currentAmount == null) {
            currentAmount = BigDecimal.ZERO;
        }

        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}