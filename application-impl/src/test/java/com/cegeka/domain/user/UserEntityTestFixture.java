package com.cegeka.domain.user;

import com.cegeka.application.Role;
import com.cegeka.application.UserTo;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserToMapper;

import java.util.Locale;
import java.util.UUID;

public class UserEntityTestFixture {
    public static final String ID = UUID.randomUUID().toString();
    public static final String EMAIL = "test@mailinator.com";
    public static final Role ROLE = Role.USER;
    public static final String PASSWORD = "testPassword";
    public static final String ROMEO_ID = UUID.randomUUID().toString();
    public static final String ROMEO_EMAIL = "romeo@mailinator.com";
    public static final Role ROMEO_ROLE = Role.USER;
    public static final String ROMEO_PASSWORD = "testPassword";
    public static final Locale ROMEO_LOCALE = Locale.CANADA;


    public static UserEntity aUserEntity(String email) {
        UserEntity entity = new UserEntity();
        entity.setPassword(PASSWORD);
        entity.setEmail(email);
        entity.addRole(ROLE);
        entity.setConfirmed(true);
        return entity;
    }

    public static UserEntity aUserEntityWithExtraRole(Role role) {
        UserEntity entity = aUserEntity(EMAIL);
        entity.addRole(role);
        return entity;
    }

    public static UserTo asUserTO() {
        return new UserToMapper().toTo(aUserEntity("romeo@mailinator.com"));
    }

    public static UserEntity romeoUser() {
        UserEntity entity = new UserEntity();
        entity.setId(ROMEO_ID);
        entity.setPassword(ROMEO_PASSWORD);
        entity.setEmail(ROMEO_EMAIL);
        entity.addRole(ROMEO_ROLE);
        entity.setConfirmed(true);
        entity.setLocale(ROMEO_LOCALE);

        return entity;
    }

    public static UserTo romeoUserTo() {
        return new UserToMapper().toTo(romeoUser());
    }
}
