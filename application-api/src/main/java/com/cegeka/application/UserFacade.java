package com.cegeka.application;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserFacade {
    void registerUser(UserTo user);

    UserTo findByEmail(String email);

    UserProfileTo getProfile(String userId);

    void confirmToken(String token);

    List<UserTo> getUsers();

    UserTo getUser(String userId);

    void updateOrCreateNewUser(UserTo userTO);

    void createNewUser(UserTo userTo);

    void updateUser(UserTo userTo);
}
