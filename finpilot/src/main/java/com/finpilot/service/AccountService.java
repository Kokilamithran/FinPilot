package com.finpilot.service;

import com.finpilot.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(Account account);

    List<Account> getAllAccounts(Long userId, boolean isAdmin);

    Account getAccountById(Long accountId, Long userId, boolean isAdmin);

    Account updateAccount(Long accountId, Account account, Long userId, boolean isAdmin);

    void deleteAccount(Long accountId, Long userId, boolean isAdmin);
}