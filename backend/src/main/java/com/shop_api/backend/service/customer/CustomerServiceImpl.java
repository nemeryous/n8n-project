package com.shop_api.backend.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop_api.backend.dto.CustomerDto;
import com.shop_api.backend.dto.CustomerRequestDto;
import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public CustomerDto createCustomer(CustomerRequestDto customerRequestDto) {
    Customer customer = new Customer();
    customer.setName(customerRequestDto.getName());
    customer.setEmail(customerRequestDto.getEmail());
    customer.setPhone(customerRequestDto.getPhone());
    customer.setAddress(customerRequestDto.getAddress());
    customer.setHashPasswords(customerRequestDto.getPassword());

    Customer savedCustomer = customerRepository.save(customer);

    return CustomerDto.fromEntity(savedCustomer);
  }

  @Override
  public CustomerDto getCustomerById(Integer id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

    return CustomerDto.fromEntity(customer);
  }

  @Override
  public List<CustomerDto> getAllCustomers() {
    List<Customer> customers = customerRepository.findAll();
    
    return CustomerDto.fromEntities(customers);
  }

  @Override
  public CustomerDto getCustomerByEmail(String email) {
    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));

    return CustomerDto.fromEntity(customer);
  }

  @Override
  public CustomerDto updateCustomer(Integer id, CustomerRequestDto customerRequestDto) {
    Customer existingCustomer = customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

    existingCustomer.setName(customerRequestDto.getName());
    existingCustomer.setEmail(customerRequestDto.getEmail());
    existingCustomer.setPhone(customerRequestDto.getPhone());
    existingCustomer.setAddress(customerRequestDto.getAddress());

    if (customerRequestDto.getPassword() != null && !customerRequestDto.getPassword().isEmpty()) {
      existingCustomer.setHashPasswords(customerRequestDto.getPassword()); 
    }

    Customer updatedCustomer = customerRepository.save(existingCustomer);

    return CustomerDto.fromEntity(updatedCustomer);
  }

  @Override
  public void deleteCustomer(Integer id) {
    Customer customer = customerRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    customerRepository.delete(customer);
  }

}