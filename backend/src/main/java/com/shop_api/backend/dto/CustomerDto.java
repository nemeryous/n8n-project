package com.shop_api.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop_api.backend.constant.CustomerSegment;
import com.shop_api.backend.entity.Customer;

import lombok.Data;

@Data
public class CustomerDto {

  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("address")
  private String address;

  @JsonProperty("customer_segment")
  private CustomerSegment customerSegment;

  public static CustomerDto fromEntity(Customer customer) {
    CustomerDto dto = new CustomerDto();
    dto.setId(customer.getId());
    dto.setName(customer.getName());
    dto.setEmail(customer.getEmail());
    dto.setPhone(customer.getPhone());
    dto.setAddress(customer.getAddress());
    dto.setCustomerSegment(customer.getCustomerSegment());

    return dto;
  }

  public static List<CustomerDto> fromEntities(List<Customer> customers) {
    return customers.stream().map(CustomerDto::fromEntity).toList();
  }
}