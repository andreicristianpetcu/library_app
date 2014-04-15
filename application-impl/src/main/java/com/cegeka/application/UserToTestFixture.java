package com.cegeka.application;

import com.google.common.collect.Sets;

import java.util.Locale;
import java.util.UUID;

//TODO: this should not be here
public class UserToTestFixture {

    public static final String ID = UUID.randomUUID().toString();
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final Role ROLE = Role.USER;
    private static final Locale LOCALE = Locale.CHINA;
    private static final String LAST_NAME = "LAST_NAME";
    private static final String EMAIL = "email@domain.com";
    private static final boolean CONFIRMED = true;

    public static UserTo aUserTo() {
        UserTo output = new UserTo();
        output.setId(ID);
        output.setFirstName(FIRST_NAME);
        output.setLastName(LAST_NAME);
        output.setEmail(EMAIL);
        output.setConfirmed(CONFIRMED);
        output.setLocale(LOCALE);
        output.setRoles(Sets.newHashSet(ROLE));
        return output;
    }
}
