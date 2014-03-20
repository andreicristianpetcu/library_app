package com.cegeka.infrastructure;

import com.google.common.collect.Lists;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Service
public class EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailTextsFiller.class);
    @Value("${smtp_port}")
    private int smtpPort;
    @Value("${smtp_server}")
    private String smtpServer;
    @Value("${smtp_user_name}")
    private String smtpUserName;
    @Value("${smtp_user_password}")
    private String smtpPassword;

    public void send(EmailTO emailTO) {
        try {
            Email email = new EmailMapper().mapFromEmailTO(emailTO);
            email.setSmtpPort(smtpPort);
            email.setHostName(smtpServer);
            email.setAuthenticator(new DefaultAuthenticator(smtpUserName, smtpPassword));
            email.setSSLOnConnect(true);
            email.send();
        } catch (EmailException e) {
            logger.error("Errors occurred while sending the email ", e);
            throw new IllegalStateException(e);
        } catch (AddressException e) {
            logger.error("Errors occurred while sending the email ", e);
            throw new IllegalStateException(e);
        }
    }

    static class EmailMapper {
        public Email mapFromEmailTO(EmailTO emailTO) throws EmailException, AddressException {
            HtmlEmail email = new HtmlEmail();
            email.setFrom(emailTO.getFrom());
            email.setTo(Lists.newArrayList(toInternetAddress(emailTO.getTos().iterator().next())));
            email.setSubject(emailTO.getSubject());
            email.setMsg(emailTO.getBody());
            email.setHtmlMsg(emailTO.getBody());
            return email;
        }

        private InternetAddress toInternetAddress(String address) throws AddressException {
            return new InternetAddress(address);
        }
    }
}
