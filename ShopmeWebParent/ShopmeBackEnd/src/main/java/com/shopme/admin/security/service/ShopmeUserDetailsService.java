package com.shopme.admin.security.service;

import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopmeUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = service.getByEmail(email);

        return new ShopmeUserDetails(user);
    }
}
