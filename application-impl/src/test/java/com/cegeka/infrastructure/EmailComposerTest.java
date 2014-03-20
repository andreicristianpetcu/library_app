package com.cegeka.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailComposerTest {

    public static final String SUBJECT = "Welcome to ginkgo, ${user_name}";
    public static final String BODY = "Hello, ${user_name}";
    public static final String FROM_EMAIL_ADDRESS = "test@test123.com";
    public static final String TO_EMAIL_ADDRESS = "to@test123.com";
    public static final String CONFIRMATION_EMAIL_SUBJECT = "confirmation-email-subject";
    public static final String CONFIRMATION_EMAIL_BODY = "confirmation-email-body";
    private static final Locale LOCALE = Locale.ENGLISH;
    private EmailComposer emailComposer;
    @Mock
    private TemplateRepository templateRepositoryMock;
    @Mock
    private EmailTextsFiller fillerMock;
    @Mock
    private EmailSender senderMock;

    @Before
    public void setUp() {
        emailComposer = new EmailComposer();
        emailComposer.setTemplateRepository(templateRepositoryMock);
        emailComposer.setEmailTextsFiller(fillerMock);
        emailComposer.setEmailSender(senderMock);
        emailComposer.setFromEmailAddress(FROM_EMAIL_ADDRESS);
    }

    @Test
    public void givenAUser_whenComposingTheEmail_thenTheEmailIsComposed() {
        when(templateRepositoryMock.getTemplateContent(CONFIRMATION_EMAIL_SUBJECT, LOCALE)).thenReturn(SUBJECT);
        when(templateRepositoryMock.getTemplateContent(CONFIRMATION_EMAIL_BODY, LOCALE)).thenReturn(BODY);

        emailComposer.sendEmail(TO_EMAIL_ADDRESS, CONFIRMATION_EMAIL_SUBJECT, CONFIRMATION_EMAIL_BODY, LOCALE, values());
        ArgumentCaptor<EmailTO> emailTOArgumentCaptor = ArgumentCaptor.forClass(EmailTO.class);

        verify(fillerMock).fillEmail(emailTOArgumentCaptor.capture(), eq(SUBJECT), eq(BODY), anyMap());
        verify(senderMock).send(emailTOArgumentCaptor.capture());
        assertThat(emailTOArgumentCaptor.getValue().getTos()).contains(TO_EMAIL_ADDRESS);
        assertThat(emailTOArgumentCaptor.getValue().getFrom()).isEqualTo(FROM_EMAIL_ADDRESS);
    }

    private Map<String, Object> values() {
        return new HashMap<String, Object>();
    }


}
