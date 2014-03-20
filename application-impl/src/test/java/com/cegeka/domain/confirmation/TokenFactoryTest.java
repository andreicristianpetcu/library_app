package com.cegeka.domain.confirmation;

import org.fest.assertions.Assertions;
import org.junit.Test;

public class TokenFactoryTest {
    @Test
    public void tokenShouldBeAtLeast32CharsLong() throws Exception {
        Assertions.assertThat(TokenFactory.generateToken().length()).isGreaterThanOrEqualTo(32);

    }
}
