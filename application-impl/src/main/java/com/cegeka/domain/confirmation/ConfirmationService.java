package com.cegeka.domain.confirmation;

import com.cegeka.application.TokenNotFoundException;
import com.cegeka.domain.users.UserEntity;
import com.cegeka.domain.users.UserRepository;
import com.cegeka.infrastructure.ConfirmationEmailCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Responsible for generating confirmation token and emailing an user this token
 * Also responsible for confirming an user based on a token
 */
@Service
public class ConfirmationService {

    @Resource
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Resource
    private ConfirmationEmailCommand confirmationEmailCommand;
    @Resource
    private UserRepository userRepository;

    public void sendConfirmationEmailTo(UserEntity user) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(TokenFactory.generateToken());
        confirmationToken.setUser(user);
        confirmationTokenRepository.saveAndFlush(confirmationToken);
        confirmationEmailCommand.send(user);
    }

    @Transactional
    public void confirmToken(String token) throws TokenNotFoundException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findOne(token);
        if (confirmationToken != null && !confirmationToken.getUser().isConfirmed()) {
            confirmationToken.getUser().setConfirmed(true);  //TODO: feature envy, fix it
            userRepository.saveAndFlush(confirmationToken.getUser());
        } else {
            throw new TokenNotFoundException("Could not find your token: " + token);
        }

    }

    public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void setConfirmationEmailCommand(ConfirmationEmailCommand confirmationEmailCommand) {
        this.confirmationEmailCommand = confirmationEmailCommand;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
