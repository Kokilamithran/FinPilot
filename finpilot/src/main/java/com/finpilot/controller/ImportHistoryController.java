package com.finpilot.controller;

import com.finpilot.dto.request.ImportHistoryRequest;
import com.finpilot.dto.response.ImportHistoryResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.ImportHistory;
import com.finpilot.entity.User;
import com.finpilot.exception.ResourceNotFoundException;
import com.finpilot.mapper.ImportHistoryMapper;
import com.finpilot.repository.AccountRepository;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.ImportHistoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/import-history")
public class ImportHistoryController {

    private final ImportHistoryService service;
    private final AccountRepository accountRepository;
    private final SecurityUtils securityUtils;

    public ImportHistoryController(ImportHistoryService service,
                                   AccountRepository accountRepository,
                                   SecurityUtils securityUtils) {
        this.service = service;
        this.accountRepository = accountRepository;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ImportHistoryResponse createImportHistory(@Valid @RequestBody ImportHistoryRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account;
        if (isAdmin) {
            account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        } else {
            account = accountRepository.findByAccountIdAndUser_UserId(request.getAccountId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }

        ImportHistory history = ImportHistoryMapper.toEntity(request, currentUser, account);

        return ImportHistoryMapper.toResponse(service.createImportHistory(history));
    }

    @GetMapping
    public List<ImportHistoryResponse> getAllImportHistory() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return service.getAllImportHistory(currentUser.getUserId(), isAdmin)
                .stream()
                .map(ImportHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ImportHistoryResponse getImportHistoryById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return ImportHistoryMapper.toResponse(service.getImportHistoryById(id, currentUser.getUserId(), isAdmin));
    }

    @PutMapping("/{id}")
    public ImportHistoryResponse updateImportHistory(
            @PathVariable Long id,
            @Valid @RequestBody ImportHistoryRequest request) {

        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account;
        if (isAdmin) {
            account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        } else {
            account = accountRepository.findByAccountIdAndUser_UserId(request.getAccountId(), currentUser.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        }

        ImportHistory history = ImportHistoryMapper.toEntity(
                request,
                currentUser,
                account
        );

        return ImportHistoryMapper.toResponse(
                service.updateImportHistory(id, history, currentUser.getUserId(), isAdmin)
        );
    }

    @DeleteMapping("/{id}")
    public String deleteImportHistory(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        service.deleteImportHistory(id, currentUser.getUserId(), isAdmin);
        return "Import History deleted successfully";
    }
}