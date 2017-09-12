package io.stardog.email.emailers;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.google.common.collect.ImmutableMap;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NonEmailerTest {
    private NonEmailer emailer;

    @Before
    public void setUp() throws Exception {
        emailer = new NonEmailer(true);
    }

    @Test
    public void sendTemplate() throws Exception {
        emailer.addTemplate(HandlebarsEmailTemplate.builder()
                .name("welcome")
                .fromEmail("support@example.com")
                .fromName("Support Team")
                .subject("Welcome, {{name}}")
                .contentHtml("{{name}}, welcome. You signed up with {{email}}. var={{var}} global={{globalVar}}")
                .build());
        emailer.addGlobalVar("globalVar", "GLOBAL");

        EmailSendResult result = emailer.sendTemplate("welcome", "bob@example.com", "Bob Smith", ImmutableMap.of("var", "foo"));
        assertEquals(31, result.getMessageId().length());
        assertTrue(result.getMessageId().startsWith("unsent-"));
    }
}