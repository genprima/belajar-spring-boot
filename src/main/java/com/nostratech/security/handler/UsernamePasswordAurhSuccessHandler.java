package com.nostratech.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostratech.security.model.AccessJwtToken;
import com.nostratech.security.util.JwtTokenFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UsernamePasswordAurhSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final JwtTokenFactory jwtTokenFactory;

    public UsernamePasswordAurhSuccessHandler(ObjectMapper objectMapper, JwtTokenFactory jwtTokenFactory) {
        this.objectMapper = objectMapper;
        this.jwtTokenFactory = jwtTokenFactory;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Map<String, String> resultmap = new HashMap<>();

        AccessJwtToken accessJwtToken = jwtTokenFactory
        .createAccessJwtToken(authentication.getName(), authentication.getAuthorities());

        resultmap.put("status", "success");
        resultmap.put("message", "Authentication successful");
        resultmap.put("token", accessJwtToken.getToken());
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), resultmap);
    }

}
