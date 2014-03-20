package com.cegeka.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
 public class LoggedInUser {

    public UserDetailsTO getLoggedInUserToBeSentToGUI() {
        return UserDetailsTO.createToFrom(getCustomUserDetails()); //TODO: does this service really need to map this TO?
    }

    public CustomUserDetails getCustomUserDetails() {
        return detailsOf(authentication());
    }

    private Authentication authentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private Collection<String> authorities(Authentication auth) {
        return getAuthoritiesNames(auth.getAuthorities());
    }

    private CustomUserDetails detailsOf(Authentication auth) {
        return (CustomUserDetails) auth.getPrincipal();
    }

    private Collection<String> getAuthoritiesNames(Collection<? extends GrantedAuthority> authorities) {
        List<String> authoritiesNames = new LinkedList<String>();

        if (authorities != null) {
            for (GrantedAuthority grantedAuthority : authorities) {
                authoritiesNames.add(grantedAuthority.getAuthority());
            }
        }

        return authoritiesNames;
    }
}