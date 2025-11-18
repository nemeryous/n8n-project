package com.shop_api.backend.security;

import java.util.Collection;
import java.util.Collections;
import com.shop_api.backend.constant.Role;
import com.shop_api.backend.entity.Customer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Custom UserDetails implementation Implements UserDetails for Spring Security integration
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(Customer customer) {
        // Get role from customer, default to USER if null
        Role role = customer.getRole() != null ? customer.getRole() : Role.USER;
        String roleName = "ROLE_" + role.name();

        Collection<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(roleName));

        return new UserPrincipal(customer.getId(), customer.getName(), customer.getEmail(),
                customer.getHashPasswords(), authorities);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

