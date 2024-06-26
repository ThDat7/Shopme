package com.shopme.admin.security;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class ShopmeUserDetails implements CustomUserDetails {
    private User user;

    public ShopmeUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public int getId() {return user.getId();}

    public boolean hasRole(String roleName) {
        Iterator<Role> roles = this.user.getRoles().iterator();

        while(roles.hasNext()) {
            Role role = roles.next();
            if (role.getName().equals(roleName)) return true;
        }

        return false;

    }

    @Override
    public Object getPrincipal() {
        return user;
    }
}
