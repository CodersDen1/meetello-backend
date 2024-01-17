package com.example.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AuthenticationFilter {

    private final JwtAuthenticationManager jwtAuthenticationManager;
    private final JwtService jwtService;
    public JwtAuthenticationFilter(JwtAuthenticationManager jwtAuthenticationManager, JwtService jwtService) {
        super(jwtAuthenticationManager , new JwtAuthenticationConverter());
        this.jwtAuthenticationManager=jwtAuthenticationManager;
        this.jwtService = jwtService;

        this.setSuccessHandler(((request, response, authentication) -> {

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //issue a new refresh token
            String userId =  ((JwtAuthentication) authentication).getPrincipal().getId();
            String refreshToken = jwtService.createRefreshJWT(userId);
            response.setHeader("Refresh-token",refreshToken);


        }));
    }
}
