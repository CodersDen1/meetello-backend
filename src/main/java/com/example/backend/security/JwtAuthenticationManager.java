package com.example.backend.security;

import com.example.backend.Users.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {

        private final JwtService jwtService;
        private final UserService userService;

    public JwtAuthenticationManager(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
       if (authentication instanceof JwtAuthentication jwtAuthentication ){
        String jwtKey = (String) jwtAuthentication.getCredentials();

        var userId = jwtService.retrieveUserId(jwtKey);

        jwtAuthentication.userEntity = userService.getUser(userId);
        jwtAuthentication.setAuthenticated(true);

         return  jwtAuthentication;

       }
       throw  new IllegalAccessError("Cannot authenticate with non-JWT authentication");
    }
}
