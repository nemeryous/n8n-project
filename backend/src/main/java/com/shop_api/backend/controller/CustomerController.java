package com.shop_api.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop_api.backend.service.customer.CustomerService;

@RestController
@RequestMapping("${api.prefix}/customers")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

}