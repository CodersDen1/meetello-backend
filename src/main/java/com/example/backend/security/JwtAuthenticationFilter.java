package com.example.backend.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AuthenticationFilter {
    public JwtAuthenticationFilter(JwtAuthenticationManager jwtAuthenticationManager) {
        super(jwtAuthenticationManager , new JwtAuthenticationConverter());

        this.setSuccessHandler(((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }));
    }
}
