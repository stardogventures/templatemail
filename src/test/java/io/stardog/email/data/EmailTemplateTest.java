package io.stardog.email.data;

import org.junit.Test;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EmailTemplateTest {

    @Test
    public void testBuilder() throws Exception {
        EmailTemplate template = EmailTemplate.builder()
                .templateName("example-template")
                .fromName("Example")
                .fromEmail("example@example.com")
                .subject("Hi there {{name}}")
                .contentHtml("<p>Hi there</p>")
                .build();

        Map<String,Object> scope = new HashMap<>();
        scope.put("name", "Bob");

        StringWriter sw = new StringWriter();
        template.getSubject().execute(sw, scope);

        assertEquals("Hi there Bob", sw.toString());
    }
}