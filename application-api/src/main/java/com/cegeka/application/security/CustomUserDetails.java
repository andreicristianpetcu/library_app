package com.cegeka.application.security;

import com.cegeka.application.Role;
import com.cegeka.application.UserTo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class CustomUserDetails implements UserDetails {
    private String userId;
    private String password;
    private String username;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
    private Locale locale;

    private CustomUserDetails() {
    }

    public static CustomUserDetails createFrom(UserTo userTo) {
        CustomUserDetails details = new CustomUserDetails();
        details.password = userTo.getPassword();
        details.userId = userTo.getId();
        details.username = userTo.getEmail();
        details.enabled = userTo.getConfirmed();
        details.locale = userTo.getLocale();

        for (Role role : userTo.getRoles()) {
            details.authorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return enabled;
    }

    public String getUserId() {
        return userId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
