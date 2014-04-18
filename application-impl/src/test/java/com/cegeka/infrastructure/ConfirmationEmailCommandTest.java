package com.cegeka.infrastructure;

import com.cegeka.domain.confirmation.ConfirmationToken;
import com.cegeka.domain.confirmation.ConfirmationTokenRepository;
import com.cegeka.domain.users.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationEmailCommandTest {

    @Mock
    protected EmailComposer emailComposerMock;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Captor
    protected ArgumentCaptor<Map<String, Object>> valuesForTemplateFillerCaptor;

    private ConfirmationEmailCommand command;

    @Before
    public void setUp() {
        command = new ConfirmationEmailCommand();
        command.setEmailComposer(emailComposerMock);
        command.setConfirmationTokenRepository(confirmationTokenRepository);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken("479d5781a2244e8ca5b6a71ce3a468bf");
        when(confirmationTokenRepository.findByUser(any(UserEntity.class))).thenReturn(confirmationToken);
    }

    @Test
    public void givenAUser_whenComposingTheEmail_thenTheEmailIsComposed() {
        command.send(user());

        verify(emailComposerMock).sendEmail(
                eq(user().getEmail()),
                eq("confirmation-email-subject"),
                eq("confirmation-email-body"),
                eq(user().getLocale()),
                valuesForTemplateFillerCaptor.capture());
        assertThat(valuesForTemplateFillerCaptor.getValue().get("confirmation_token")).isNotNull();
    }

    private UserEntity user() {
        UserEntity user = new UserEntity();
        user.setEmail("gigel@domeniu.com");
        user.setLocale(Locale.ENGLISH);
        return user;
    }
}
