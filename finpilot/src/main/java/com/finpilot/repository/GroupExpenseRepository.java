package com.finpilot.repository;

import com.finpilot.entity.GroupExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupExpenseRepository extends JpaRepository<GroupExpense, Long> {
    List<GroupExpense> findByCreatedBy_UserId(Long userId);

    @Query("SELECT e FROM GroupExpense e WHERE e.groupExpenseId = :groupExpenseId AND e.createdBy.userId = :userId")
    Optional<GroupExpense> findByGroupExpenseIdAndUserId(@Param("groupExpenseId") Long groupExpenseId, @Param("userId") Long userId);
}