package com.shopme.security.oauth;

import com.shopme.common.entity.AuthenticationType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomerOAuth2User implements OAuth2User {
    private OAuth2User oAuth2User;

    private String clientName;
    private String fullName;

    public CustomerOAuth2User(OAuth2User oAuth2User, String clientName) {
        this.oAuth2User = oAuth2User;
        this.clientName = clientName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getFullName() {
        return fullName != null ? fullName : oAuth2User.getAttribute("name");

    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getUsername() {
        return oAuth2User.getAttribute("email");
    }
}
