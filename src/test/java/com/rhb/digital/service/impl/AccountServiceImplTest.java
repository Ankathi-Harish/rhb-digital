package com.rhb.digital.service.impl;
import com.rhb.digital.model.Account;
import com.rhb.digital.model.Customer;
import com.rhb.digital.repository.AccountRepository;
import com.rhb.digital.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    // Test for createAccount()
    @Test
    void testCreateAccount_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Account account = new Account();
        account.setCustomer(customer);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account createdAccount = accountService.createAccount(account);

        // Assert
        assertNotNull(createdAccount);
        assertEquals("John Doe", createdAccount.getCustomer().getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccount_CustomerNotFound() {
        // Arrange
        Account account = new Account();
        Customer customer = new Customer();
        customer.setId(1L);
        account.setCustomer(customer);

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.createAccount(account);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(accountRepository, never()).save(account);
    }

    // Test for getAllAccounts()
    @Test
    void testGetAllAccounts() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Account account = new Account();
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account), pageable, 1);
        when(accountRepository.findAll(pageable)).thenReturn(accountPage);

        // Act
        Page<Account> result = accountService.getAllAccounts(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(accountRepository, times(1)).findAll(pageable);
    }

    // Test for getAccountById()
    @Test
    void testGetAccountById_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        Account result = accountService.getAccountById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAccountById_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.getAccountById(1L);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
    }

    // Test for updateAccount()
    @Test
    void testUpdateAccount_Success() {
        // Arrange
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        existingAccount.setAccountNumber("12345");
        existingAccount.setBalance(100.0);

        Account updatedDetails = new Account();
        updatedDetails.setAccountNumber("54321");
        updatedDetails.setBalance(200.0);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        // Act
        Account result = accountService.updateAccount(1L, updatedDetails);

        // Assert
        assertEquals("54321", result.getAccountNumber());
        assertEquals(200.0, result.getBalance());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(existingAccount);
    }

    // Test for deleteAccount()
    @Test
    void testDeleteAccount_Success() {
        // Arrange
        Account account = new Account();
        account.setId(1L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        // Act
        accountService.deleteAccount(1L);

        // Assert
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void testDeleteAccount_AccountNotFound() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            accountService.deleteAccount(1L);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, never()).delete(any());
    }
}
