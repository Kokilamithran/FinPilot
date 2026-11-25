package com.finpilot.repository;

import com.finpilot.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser_UserId(Long userId);
    Optional<Transaction> findByTransactionIdAndUser_UserId(Long transactionId, Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.userId = :userId " +
           "AND t.category.categoryId = :categoryId " +
           "AND t.transactionType = 'EXPENSE' " +
           "AND t.transactionDate >= :startDate " +
           "AND t.transactionDate <= :endDate")
    BigDecimal calculateSpentAmount(@Param("userId") Long userId,
                                    @Param("categoryId") Long categoryId,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate);
}