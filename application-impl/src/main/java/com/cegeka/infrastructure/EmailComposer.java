package com.cegeka.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Map;

@Service
public class EmailComposer {

    @Resource
    private TemplateRepository templateRepository;
    @Resource
    private EmailTextsFiller emailTextsFiller;
    @Resource
    private EmailSender emailSender;
    @Value("${from_email_address}")
    private String fromEmailAddress;

    public void sendEmail(String to, String subjectTemplateName, String bodyTemplateName, Locale locale, Map<String, Object> values) {
        String subjectTemplateContent = templateRepository.getTemplateContent(subjectTemplateName, locale);
        String bodyTemplateContent = templateRepository.getTemplateContent(bodyTemplateName, locale);

        EmailTO emailTO = new EmailTO();
        emailTO.setFrom(fromEmailAddress);
        emailTO.addTo(to);

        emailTextsFiller.fillEmail(emailTO, subjectTemplateContent, bodyTemplateContent, values);
        emailSender.send(emailTO);
    }

    public void setTemplateRepository(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public void setEmailTextsFiller(EmailTextsFiller emailTextsFiller) {
        this.emailTextsFiller = emailTextsFiller;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }
}
