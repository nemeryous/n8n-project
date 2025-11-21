package com.shop_api.backend.config;

import com.shop_api.backend.constant.Role;
import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {

  private final CustomerRepository customerRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) throws Exception {
    // Kiểm tra xem admin đã tồn tại chưa
    String adminEmail = "admin@yourapp.com";
    Optional<Customer> existingAdmin = customerRepository.findByEmail(adminEmail);

    if (existingAdmin.isEmpty()) {
      Customer admin = new Customer();
      admin.setName("Super Admin");
      admin.setEmail(adminEmail);
      admin.setPhone("0999999999");
      admin.setAddress("Server Room");

      admin.setHashPasswords(passwordEncoder.encode("123456"));
      admin.setRole(Role.ADMIN);

      customerRepository.save(admin);

      System.out.println("✅ ADMIN User created successfully: " + adminEmail);
    } else {
      System.out.println("ℹ️ ADMIN User already exists.");
    }
  }
}