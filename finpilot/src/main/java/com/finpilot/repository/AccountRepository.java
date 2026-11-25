package com.finpilot.repository;

import com.finpilot.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser_UserId(Long userId);
    Optional<Account> findByAccountIdAndUser_UserId(Long accountId, Long userId);
}