package com.cegeka.infrastructure;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Fills in subject/body/extra headers, given templates and values
 */
@Service
public class EmailTextsFiller {

    private static final Logger logger = LoggerFactory.getLogger(EmailTextsFiller.class);

    private static final String SUBJECT = "subject_template";
    private static final String BODY = "body_template";

    public void fillEmail(EmailTO emailTO, String subjectTemplateContent, String bodyTemplateContent, Map<String, Object> values) {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(getTemplateLoader(subjectTemplateContent, bodyTemplateContent));
        try {
            Template subjectTemplate = cfg.getTemplate(SUBJECT);
            emailTO.setSubject(getTemplateProcessingContents(subjectTemplate, values));
            Template bodyTemplate = cfg.getTemplate(BODY);
            emailTO.setBody (getTemplateProcessingContents(bodyTemplate, values));

        } catch (TemplateException e) {
            logger.error("Errors occurred while processing template ", e);
            throw new IllegalStateException(e);
        } catch (IOException e) {
            logger.error("Errors occurred while loading template ", e);
            throw new IllegalStateException(e);
        }

    }

    private String getTemplateProcessingContents(Template template, Object values) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        template.process(values, writer);
        return writer.toString();


    }

    private TemplateLoader getTemplateLoader(String subjectTemplateContent, String bodyTemplateContent) {
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate(SUBJECT, subjectTemplateContent);
        stringLoader.putTemplate(BODY, bodyTemplateContent);
        return stringLoader;
    }


}
