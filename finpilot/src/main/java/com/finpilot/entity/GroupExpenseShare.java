package com.finpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "group_expense_shares",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_expense_user",
                        columnNames = {"expense_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupExpenseShare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shareId;

    @ManyToOne
    @JoinColumn(name = "expense_id", nullable = false)
    private GroupExpense groupExpense;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal shareAmount;

    @Column(nullable = false)
    private Boolean isSettled = false;

    private LocalDateTime settledAt;

    @Column(length = 50)
    private String settledMode;

    @PrePersist
    protected void onCreate() {
        if (isSettled == null) {
            isSettled = false;
        }
    }
}