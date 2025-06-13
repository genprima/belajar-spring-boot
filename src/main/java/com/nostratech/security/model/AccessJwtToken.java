package com.nostratech.security.model;

import io.jsonwebtoken.Claims;

public class AccessJwtToken implements Token {

    private final String rawToken;
    private final Claims claims;

    public AccessJwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    @Override
    public String getToken() {
        return rawToken;
    }

    public Claims getClaims() {
        return claims;
    }

}
