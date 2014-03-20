package com.cegeka.infrastructure;

import com.cegeka.domain.confirmation.ConfirmationToken;
import com.cegeka.domain.confirmation.ConfirmationTokenRepository;
import com.cegeka.domain.users.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
public class ConfirmationEmailCommand {

    private static final String CONFIRMATION_TOKEN_VARIABLE_NAME = "confirmation_token";
    private final static Logger  logger = LoggerFactory.getLogger(ConfirmationEmailCommand.class);
    @Resource
    private EmailComposer emailComposer;
    @Resource
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Value("${application_url}")
    private String applicationUrl;

    public void send(UserEntity user) {
        emailComposer.sendEmail(user.getEmail(), "confirmation-email-subject", "confirmation-email-body", user.getLocale(), getTemplateValuesFor(user));
    }

    private HashMap<String, Object> getTemplateValuesFor(UserEntity userEntity) {
        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("full_name", userEntity.getProfile().getFullName());
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(userEntity);
        if (confirmationToken != null) {
            values.put(CONFIRMATION_TOKEN_VARIABLE_NAME, getConfirmationUrl(confirmationToken.getToken()));
        } else {
            String message = "When sending a confirmation email there should be a ConfirmationToken generated for the user, but I can't find it";
            logger.error(message);
            throw new IllegalStateException(message);
        }
        return values;
    }

    private String getConfirmationUrl(String token) {
        return applicationUrl + "#/confirm-token/" + token;
    }

    public void setConfirmationTokenRepository(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void setEmailComposer(EmailComposer emailComposer) {
        this.emailComposer = emailComposer;
    }
}
