package com.example.backend.security;


import com.example.backend.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AppSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtService jwtService, UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequest)-> authorizeHttpRequest
                        .requestMatchers(HttpMethod.POST,"/api/users/**").permitAll().anyRequest().authenticated()
                ).formLogin(Customizer.withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class);
        return http.build();
    }
}
