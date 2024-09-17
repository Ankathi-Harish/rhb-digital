package com.rhb.digital.service.impl;

import com.rhb.digital.model.Customer;
import com.rhb.digital.repository.CustomerRepository;
import com.rhb.digital.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        customer.setName(customerDetails.getName());
        return customerRepository.save(customer);
    }
    @Override
    public Customer patchCustomer(Long id, Map<String, Object> updates) {
        Customer customer = getCustomerById(id);

        if (updates.containsKey("name")) {
            customer.setName((String) updates.get("name"));
        }
        if (updates.containsKey("dateOfBirth")) {
            customer.setDateOfBirth((String) updates.get("dateOfBirth"));
        }
        if (updates.containsKey("mobileNumber")) {
            customer.setMobileNumber((String) updates.get("mobileNumber"));
        }
        if (updates.containsKey("address")) {
            customer.setAddress((String) updates.get("address"));
        }

        return customerRepository.save(customer);
    }
    @Override
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}