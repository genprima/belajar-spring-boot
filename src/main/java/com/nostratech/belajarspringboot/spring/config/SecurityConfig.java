package com.nostratech.belajarspringboot.spring.config;

import java.security.Key;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostratech.security.filter.UsernamePasswordAuthProcessingFilter;
import com.nostratech.security.util.JwtTokenFactory;

import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String AUTH_URL = "/api/v1/auth/login";
    private static final String V1_URL = "/api/v1/**";
    private static final String V2_URL = "/api/v2/**";

    @Bean
    public Key secret(){
        byte[] keyBytes = Base64.getDecoder().decode("1234567890");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public JwtTokenFactory jwtTokenFactory(Key secret){
        return new JwtTokenFactory(secret);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public UsernamePasswordAuthProcessingFilter usernamePasswordAuthenticationFilter(
            ObjectMapper objectMapper,
            AuthenticationSuccessHandler successHandler,
            AuthenticationFailureHandler failureHandler,
            AuthenticationManager authenticationManager,
            JwtTokenFactory jwtTokenFactory) {
        UsernamePasswordAuthProcessingFilter filter = new UsernamePasswordAuthProcessingFilter(AUTH_URL, objectMapper,
                successHandler, failureHandler, jwtTokenFactory);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            UsernamePasswordAuthProcessingFilter usernamePasswordAuthProcessingFilter) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(V1_URL, V2_URL)
                .authenticated()
                .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(usernamePasswordAuthProcessingFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
