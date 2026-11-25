package com.finpilot.repository;

import com.finpilot.entity.GroupExpenseShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupExpenseShareRepository extends JpaRepository<GroupExpenseShare, Long> {
    List<GroupExpenseShare> findByUser_UserId(Long userId);
    Optional<GroupExpenseShare> findByShareIdAndUser_UserId(Long shareId, Long userId);
}