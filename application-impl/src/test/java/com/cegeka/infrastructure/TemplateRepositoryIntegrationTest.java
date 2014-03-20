package com.cegeka.infrastructure;

import com.cegeka.IntegrationTest;
import org.fest.assertions.Assertions;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Locale;

public class TemplateRepositoryIntegrationTest extends IntegrationTest {

    private static final String CONTENT = "content";
    private static final String TEMPLATE_NAME = "test-template";
    @Resource
    private TemplateRepository repository;

    @Test
    public void givenATemplateIdAndALocale_itReturnsTheContent() throws Exception {
        String content = repository.getTemplateContent(TEMPLATE_NAME, Locale.ENGLISH);

        Assertions.assertThat(content).isEqualTo(CONTENT);
    }
}
