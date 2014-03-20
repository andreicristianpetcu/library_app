package com.cegeka.application.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

// TODO: let's throw this away
public class UserDetailsTO {
    private String userId;
    private String username;
    private Set<String> roles;
    private Locale locale;

    private UserDetailsTO() {
    }

    public static UserDetailsTO createToFrom(CustomUserDetails details) {
        UserDetailsTO to = new UserDetailsTO();
        to.userId = details.getUserId();
        to.username = details.getUsername();
        to.roles = roles(details.getAuthorities());
        return to;

    }

    private static Set<String> roles(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = Sets.newHashSet();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }

        return roles;
    }

    public String getUserId() {
        return userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Locale getUserLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
