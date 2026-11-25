package com.finpilot.repository;

import com.finpilot.entity.ImportHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
    List<ImportHistory> findByUser_UserId(Long userId);
    Optional<ImportHistory> findByImportIdAndUser_UserId(Long importId, Long userId);
}