package com.rhb.digital.service;

import com.rhb.digital.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface AccountService {
    Account createAccount(Account account);
    Page<Account> getAllAccounts(Pageable pageable);
    Account getAccountById(Long id);
    Account updateAccount(Long id, Account accountDetails);
    Account patchAccount(Long id, Map<String, Object> updates);
    void deleteAccount(Long id);
}