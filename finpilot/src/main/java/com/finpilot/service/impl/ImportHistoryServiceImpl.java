package com.finpilot.service.impl;

import com.finpilot.entity.ImportHistory;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.repository.ImportHistoryRepository;
import com.finpilot.service.ImportHistoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportHistoryServiceImpl implements ImportHistoryService {

    private final ImportHistoryRepository repository;

    public ImportHistoryServiceImpl(ImportHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public ImportHistory createImportHistory(ImportHistory history) {
        return repository.save(history);
    }

    @Override
    public List<ImportHistory> getAllImportHistory(Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findAll();
        }
        return repository.findByUser_UserId(userId);
    }

    @Override
    public ImportHistory getImportHistoryById(Long id, Long userId, boolean isAdmin) {
        if (isAdmin) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Import History not found"));
        }
        return repository.findByImportIdAndUser_UserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Import History not found"));
    }

    @Override
    public ImportHistory updateImportHistory(Long id, ImportHistory history, Long userId, boolean isAdmin) {

        ImportHistory existing = getImportHistoryById(id, userId, isAdmin);

        existing.setAccount(history.getAccount());
        existing.setFileName(history.getFileName());
        existing.setFileFormat(history.getFileFormat());
        existing.setTotalRecords(history.getTotalRecords());
        existing.setSuccessCount(history.getSuccessCount());
        existing.setDuplicateCount(history.getDuplicateCount());
        existing.setStatus(history.getStatus());
        existing.setRemarks(history.getRemarks());

        return repository.save(existing);
    }

    @Override
    public void deleteImportHistory(Long id, Long userId, boolean isAdmin) {
        ImportHistory existing = getImportHistoryById(id, userId, isAdmin);
        repository.delete(existing);
    }
}