package com.cegeka.domain.confirmation;

import com.cegeka.application.TokenNotFoundException;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.ConfirmationEmailCommand;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationServiceTest {

    private String TOKEN = "8b9ca1e285514959abdecb528317a592";
    private ConfirmationToken confirmationToken;
    private UserEntity user;
    private ConfirmationService confirmationService;
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Mock
    private ConfirmationEmailCommand confirmationEmailCommand;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        user = new UserEntity();
        confirmationService = new ConfirmationService();
        confirmationService.setConfirmationTokenRepository(confirmationTokenRepository);
        confirmationService.setConfirmationEmailCommand(confirmationEmailCommand);
        confirmationService.setUserRepository(userRepository);

        confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(TOKEN);
        confirmationToken.setUser(user);
    }

    @Test
    public void testSendConfirmationEmailTo() throws Exception {
        confirmationService.sendConfirmationEmailTo(user);

        ArgumentCaptor<ConfirmationToken> confirmationTokenArgumentCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        Mockito.verify(confirmationTokenRepository).saveAndFlush(confirmationTokenArgumentCaptor.capture());
        Mockito.verify(confirmationEmailCommand).send(user);

        Assertions.assertThat(confirmationTokenArgumentCaptor.getValue().getUser()).isSameAs(user);
        Assertions.assertThat(confirmationTokenArgumentCaptor.getValue().getToken()).isNotEmpty();
        Assertions.assertThat(confirmationTokenArgumentCaptor.getValue().getState()).isEqualTo(ConfirmationToken.ConfirmationState.GENERATED);
    }

    @Test
    public void testUserIsConfirmedAfterConfirmationTokenIsConfirmed() {

        Mockito.when(confirmationTokenRepository.findOne(TOKEN)).thenReturn(confirmationToken);

        confirmationService.confirmToken(TOKEN);
        Assertions.assertThat(user.isConfirmed()).isTrue();
    }

    @Test(expected = TokenNotFoundException.class)
    public void testExceptionIsThrownWhenTokenIsNotFound() {

        Mockito.when(confirmationTokenRepository.findOne(TOKEN)).thenReturn(null);
        confirmationService.confirmToken(TOKEN);
    }
}
