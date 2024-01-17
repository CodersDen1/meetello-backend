package com.example.backend.security;

import com.auth0.jwt.algorithms.Algorithm;
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

        //validating if it is a access token or refresh token
           Algorithm algorithm = jwtService.retrieveAlgorithm(jwtKey);
           boolean isRefreshToken = algorithm== jwtService.refreshAlgorithm();

        if(isRefreshToken){
            jwtAuthentication.isRefreshToken(true);
            return jwtAuthentication;
        }else {
            jwtAuthentication.isRefreshToken(false);
        }
           var userId = jwtService.retrieveUserId(jwtKey);
           jwtAuthentication.userEntity = userService.getUser(userId);
           jwtAuthentication.setAuthenticated(true);
         //validate the access token
         return  jwtAuthentication;
       }
       throw  new IllegalAccessError("Cannot authenticate with non-JWT authentication");
    }
}
