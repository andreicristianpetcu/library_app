package com.cegeka.infrastructure;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EmailTextsFillerTest {

    @Test
    public void givenSubjectAndBodyTemplates_whenCorrectValuesSupplied_EmailTextsAreProperlyFilledIn(){
        EmailTO emailTO = new EmailTO();
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("subject_value_1", "subject value 1");
        values.put("subject_value_2", "subject value 2");
        values.put("body_value_1", "body value 1");
        values.put("body_value_2", "body value 2");


        EmailTextsFiller emailTextsFiller = new EmailTextsFiller();

        emailTextsFiller.fillEmail(emailTO,"Hello, ${subject_value_1} this is ${subject_value_2}", "This is the body ${body_value_1}, ${body_value_2}", values);
        Assertions.assertThat(emailTO.getSubject()).isEqualTo("Hello, subject value 1 this is subject value 2");
        Assertions.assertThat(emailTO.getBody()).isEqualTo("This is the body body value 1, body value 2");

    }
}
