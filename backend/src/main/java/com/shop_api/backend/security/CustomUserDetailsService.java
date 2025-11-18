package com.shop_api.backend.security;

import com.shop_api.backend.entity.Customer;
import com.shop_api.backend.exception.ResourceNotFoundException;
import com.shop_api.backend.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Custom UserDetailsService implementation Loads user-specific data for authentication
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy người dùng với email: " + email));

        return UserPrincipal.create(customer);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

        return UserPrincipal.create(customer);
    }
}

