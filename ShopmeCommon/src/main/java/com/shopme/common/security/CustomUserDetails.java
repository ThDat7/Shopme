package com.shopme.common.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    public Object getPrincipal();
}
