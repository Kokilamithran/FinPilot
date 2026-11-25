package com.finpilot.repository;

import com.finpilot.entity.GoalContribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GoalContributionRepository extends JpaRepository<GoalContribution, Long> {
    List<GoalContribution> findByFinancialGoal_User_UserId(Long userId);
    Optional<GoalContribution> findByContributionIdAndFinancialGoal_User_UserId(Long contributionId, Long userId);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM GoalContribution c WHERE c.financialGoal.goalId = :goalId")
    BigDecimal sumAmountByFinancialGoal_GoalId(@Param("goalId") Long goalId);

    @Modifying
    @Query("DELETE FROM GoalContribution c WHERE c.financialGoal.goalId = :goalId")
    void deleteByFinancialGoal_GoalId(@Param("goalId") Long goalId);
}