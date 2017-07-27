package io.stardog.email.data;

import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HandlebarsEmailTemplateTest {

    @Test
    public void testBuilder() throws Exception {
        HandlebarsEmailTemplate template = HandlebarsEmailTemplate.builder()
                .name("example-template")
                .fromName("Example")
                .fromEmail("example@example.com")
                .subject("Hi there {{name}}")
                .contentHtml("<p>Hi there</p>")
                .build();

        Map<String,Object> scope = new HashMap<>();
        scope.put("name", "Bob");

        assertEquals("Hi there Bob", template.getSubject().apply(scope));
    }

    @Test
    public void testBuilderWithFiles() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("template1.html").getFile());

        HandlebarsEmailTemplate template = HandlebarsEmailTemplate.builder()
                .name("example-template")
                .fromName(file)
                .fromEmail(file)
                .subject(file)
                .contentHtml(file)
                .build();

        Map<String,Object> scope = new HashMap<>();
        scope.put("name", "Bob");

        assertEquals("<p>Dear Bob,</p>\n\n<p>This is a template.</p>", template.getSubject().apply(scope));

    }
}