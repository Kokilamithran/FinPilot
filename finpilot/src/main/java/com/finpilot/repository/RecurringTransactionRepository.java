package com.finpilot.repository;

import com.finpilot.entity.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {
    List<RecurringTransaction> findByUser_UserId(Long userId);
    Optional<RecurringTransaction> findByRecurringIdAndUser_UserId(Long recurringId, Long userId);
}