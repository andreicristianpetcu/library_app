package com.cegeka.application.security;

import com.cegeka.application.UserFacade;
import com.cegeka.application.UserTo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
// TODO: should be merged with LoggedInService
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Resource
    private UserFacade userFacade;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserTo user = userFacade.findByEmail(email);
        return CustomUserDetails.createFrom(user);
    }

    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }
}
