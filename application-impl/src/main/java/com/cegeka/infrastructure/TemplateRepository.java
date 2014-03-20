package com.cegeka.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

@Service
public class TemplateRepository {
    private static final Logger logger = LoggerFactory.getLogger(TemplateRepository.class);
    @Autowired
    private ApplicationContext context;
    @Value("${templates_location}")
    private String baseTemplatePath;

    public String getTemplateContent(String templateName, Locale locale) {
        String location = getLocation(templateName, locale);
        InputStream resourceAsStream = TemplateRepository.class.getClassLoader().getResourceAsStream(location);
        if (resourceAsStream == null) {
            logger.error("Resource not found: "+ location);
            throw new RuntimeException("Resource not found: " + location);
        }

        return new Scanner(resourceAsStream, "UTF-8").useDelimiter("\\A").next();
    }

    public void setBaseTemplatePath(String baseTemplatePath) {
        this.baseTemplatePath = baseTemplatePath;
    }

    private String getLocation(String templateName, Locale locale) {
        return new StringBuilder()
                .append(baseTemplatePath)
                .append("/")
                .append(templateName)
                .append("_")
                .append(locale.getLanguage())
                .append(".ftl")
                .toString();
    }

}
