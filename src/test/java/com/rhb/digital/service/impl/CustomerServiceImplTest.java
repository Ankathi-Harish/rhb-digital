package com.rhb.digital.service.impl;

import com.rhb.digital.model.Customer;
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
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    // Test for createCustomer()
    @Test
    void testCreateCustomer_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.save(customer)).thenReturn(customer);

        // Act
        Customer createdCustomer = customerService.createCustomer(customer);

        // Assert
        assertNotNull(createdCustomer);
        assertEquals("John Doe", createdCustomer.getName());
        verify(customerRepository, times(1)).save(customer);
    }

    // Test for getAllCustomers()
    @Test
    void testGetAllCustomers() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Customer customer = new Customer();
        Page<Customer> customerPage = new PageImpl<>(Collections.singletonList(customer), pageable, 1);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);

        // Act
        Page<Customer> result = customerService.getAllCustomers(pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        verify(customerRepository, times(1)).findAll(pageable);
    }

    // Test for getCustomerById()
    @Test
    void testGetCustomerById_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(1L);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
    }

    // Test for updateCustomer()
    @Test
    void testUpdateCustomer_Success() {
        // Arrange
        Customer existingCustomer = new Customer();
        existingCustomer.setId(1L);
        existingCustomer.setName("John Doe");

        Customer updatedDetails = new Customer();
        updatedDetails.setName("Jane Doe");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);

        // Act
        Customer result = customerService.updateCustomer(1L, updatedDetails);

        // Assert
        assertEquals("Jane Doe", result.getName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    // Test for deleteCustomer()
    @Test
    void testDeleteCustomer_Success() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void testDeleteCustomer_CustomerNotFound() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(1L);
        });

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).delete(any());
    }
}
