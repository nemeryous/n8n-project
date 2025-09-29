package com.shop_api.backend.service.customer;

import com.shop_api.backend.dto.CustomerDto;
import com.shop_api.backend.dto.CustomerRequestDto;
import java.util.List;

public interface CustomerService {
  CustomerDto createCustomer(CustomerRequestDto customerRequestDto);

  CustomerDto getCustomerById(Integer id);

  List<CustomerDto> getAllCustomers();

  CustomerDto getCustomerByEmail(String email);

  CustomerDto updateCustomer(Integer id, CustomerRequestDto customerRequestDto);

  void deleteCustomer(Integer id);
}