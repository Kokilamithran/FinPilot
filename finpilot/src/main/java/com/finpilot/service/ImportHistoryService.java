package com.finpilot.service;

import com.finpilot.entity.ImportHistory;

import java.util.List;

public interface ImportHistoryService {

    ImportHistory createImportHistory(ImportHistory history);

    List<ImportHistory> getAllImportHistory(Long userId, boolean isAdmin);

    ImportHistory getImportHistoryById(Long id, Long userId, boolean isAdmin);

    ImportHistory updateImportHistory(Long id, ImportHistory history, Long userId, boolean isAdmin);

    void deleteImportHistory(Long id, Long userId, boolean isAdmin);
}