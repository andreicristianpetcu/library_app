package com.cegeka.domain.confirmation;

import com.cegeka.IntegrationTest;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.cegeka.domain.user.UserEntityTestFixture.aUserEntity;

@Transactional
public class ConfirmationTokenRepositoryIntegrationTest extends IntegrationTest {

    UserEntity userEntity;
    ConfirmationToken confirmationToken;
    @Resource
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Resource
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
        userEntity = aUserEntity("romeo@mailinator.com");
        userEntity = userRepository.saveAndFlush(userEntity);

        confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(userEntity);
        confirmationToken.setToken(TokenFactory.generateToken());
    }

    @Test
    public void iCanPersistAConfirmationToken() {
        confirmationTokenRepository.saveAndFlush(confirmationToken);

        Assertions.assertThat(confirmationTokenRepository.findOne(confirmationToken.getToken())).isNotNull();
    }

    @Test
    public void iCanFindATokenGivenItsUser() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(userEntity);
        confirmationToken.setToken(TokenFactory.generateToken());
        confirmationTokenRepository.saveAndFlush(confirmationToken);

        Assertions.assertThat(confirmationTokenRepository.findByUser(userEntity)).isNotNull();
    }
}