package com.finpilot.controller;

import com.finpilot.dto.request.AccountRequest;
import com.finpilot.dto.response.AccountResponse;
import com.finpilot.entity.Account;
import com.finpilot.entity.User;
import com.finpilot.mapper.AccountMapper;
import com.finpilot.security.SecurityUtils;
import com.finpilot.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final SecurityUtils securityUtils;

    public AccountController(AccountService accountService,
                             SecurityUtils securityUtils) {
        this.accountService = accountService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public AccountResponse createAccount(@Valid @RequestBody AccountRequest request) {

        User currentUser = securityUtils.getCurrentUser();

        Account account = AccountMapper.toEntity(request, currentUser);

        Account saved = accountService.createAccount(account);

        return AccountMapper.toResponse(saved);
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return accountService.getAllAccounts(currentUser.getUserId(), isAdmin)
                .stream()
                .map(AccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        return AccountMapper.toResponse(
                accountService.getAccountById(id, currentUser.getUserId(), isAdmin)
        );
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id,
                                         @Valid @RequestBody AccountRequest request) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        Account account = AccountMapper.toEntity(request, currentUser);

        Account updated = accountService.updateAccount(id, account, currentUser.getUserId(), isAdmin);

        return AccountMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = securityUtils.isAdmin();

        accountService.deleteAccount(id, currentUser.getUserId(), isAdmin);
        return "Account deleted successfully";
    }
}