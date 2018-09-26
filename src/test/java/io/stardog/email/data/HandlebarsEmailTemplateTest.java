package io.stardog.email.data;

import com.google.common.collect.ImmutableMap;
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

    @Test
    public void testBuilderEnsureSubjectLineEscaping() throws Exception {
        HandlebarsEmailTemplate template = HandlebarsEmailTemplate.builder()
                .name("test")
                .fromName("From name: {{name}}")
                .fromEmail("From email: {{name}}")
                .subject("Subject: {{name}}")
                .contentText("Text: {{name}}")
                .contentHtml("Html: {{name}}")
                .build();
        Map<String,Object> context = ImmutableMap.of("name", "Jane O'Leary & Co");
        assertEquals("From name: Jane O'Leary & Co", template.getFromName().apply(context));
        assertEquals("From email: Jane O'Leary & Co", template.getFromEmail().apply(context));
        assertEquals("Subject: Jane O'Leary & Co", template.getSubject().apply(context));
        assertEquals("Text: Jane O'Leary & Co", template.getContentText().apply(context));
        assertEquals("Html: Jane O&#x27;Leary &amp; Co", template.getContentHtml().apply(context));
    }
}