package com.shop_api.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

/**
 * Filter to handle API Key authentication for external services like n8n
 */
@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    @Value("${n8n.api-key}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String requestApiKey = request.getHeader("X-API-KEY");

        // If API Key matches, authenticate as ADMIN
        if (apiKey != null && !apiKey.isEmpty() && apiKey.equals(requestApiKey)) {
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            
            // Create a dummy UserPrincipal for n8n
            UserPrincipal principal = new UserPrincipal(
                0, // ID 0 for system/n8n user
                "N8N Integration",
                "n8n@system",
                null,
                authorities
            );

            var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
