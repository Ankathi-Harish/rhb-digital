package com.rhb.digital.service.impl;

import com.rhb.digital.model.Account;
import com.rhb.digital.model.Customer;
import com.rhb.digital.repository.AccountRepository;
import com.rhb.digital.repository.CustomerRepository;
import com.rhb.digital.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    @Override
    public Account createAccount(Account account) {
        // Fetch the customer from the database

        Customer customer = customerRepository.findById(account.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Make sure all fields of customer are populated
        logger.info("Customer details: " + customer.getName() + ", " + customer.getDateOfBirth());

        account.setCustomer(customer); // Ensure customer is fully populated
        return accountRepository.save(account);
    }

    @Override
    public Page<Account> getAllAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Account updateAccount(Long id, Account accountDetails) {
        Account account = getAccountById(id);
        account.setAccountNumber(accountDetails.getAccountNumber());
        account.setBalance(accountDetails.getBalance());
        account.setCustomer(accountDetails.getCustomer());
        return accountRepository.save(account);
    }

    @Override
    public Account patchAccount(Long id, Map<String, Object> updates) {
        Account account = getAccountById(id);

        if (updates.containsKey("accountNumber")) {
            account.setAccountNumber((String) updates.get("accountNumber"));
        }
        if (updates.containsKey("balance")) {
            account.setBalance((Double) updates.get("balance"));
        }
        // Handle other fields if needed

        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }
}