package com.rhb.digital.service;

import com.rhb.digital.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Page<Customer> getAllCustomers(Pageable pageable);
    Customer getCustomerById(Long id);
    Customer updateCustomer(Long id, Customer customerDetails);
    Customer patchCustomer(Long id, Map<String, Object> updates);
    void deleteCustomer(Long id);
}