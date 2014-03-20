package com.cegeka.domain.user;

import com.cegeka.application.Role;
import com.cegeka.application.UserTo;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserToMapper;

import java.util.UUID;

public class UserEntityTestFixture {
    public static final String ID = UUID.randomUUID().toString();
    public static final String EMAIL = "test@mailinator.com";
    public static final Role ROLE = Role.USER;
    public static final String PASSWORD = "testPassword";

    private UserEntityTestFixture() {
    }

    public static UserEntity aUserEntity() {
        UserEntity entity = new UserEntity();
        entity.setPassword(PASSWORD);
        entity.setEmail(EMAIL);
        entity.addRole(ROLE);
        entity.setConfirmed(true);
        return entity;
    }

    public static UserEntity aUserEntityWithExtraRole(Role role) {
        UserEntity entity = aUserEntity();
        entity.addRole(role);
        return entity;
    }

    public static UserTo asUserTO() {
        return new UserToMapper().toTo(aUserEntity());
    }
}
