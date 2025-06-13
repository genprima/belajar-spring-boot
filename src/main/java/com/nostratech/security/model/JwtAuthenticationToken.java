package com.nostratech.security.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken{

   private UserDetails userDetails;

    public JwtAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(false);
        this.setDetails(userDetails);
    }

    @Override
    public Object getCredentials() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCredentials'");
    }

    @Override
    public Object getPrincipal() {
        throw new UnsupportedOperationException("Unimplemented method 'getPrincipal'");
    }

    
}
