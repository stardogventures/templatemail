package io.stardog.email.emailers;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
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

public class AmazonSesEmailerTest {
    private AmazonSimpleEmailServiceClient sesClient;
    private AmazonSesEmailer emailer;

    @Before
    public void setUp() throws Exception {
        sesClient = mock(AmazonSimpleEmailServiceClient.class);
        emailer = new AmazonSesEmailer(sesClient);
    }

    @Test
    public void sendTemplate() throws Exception {
        emailer.addTemplate(HandlebarsEmailTemplate.builder()
                .templateName("welcome")
                .fromEmail("support@example.com")
                .fromName("Support Team")
                .subject("Welcome, {name}")
                .contentHtml("{name}, welcome. You signed up with {email}. var={var} global={globalVar}")
                .build());
        emailer.addGlobalVar("globalVar", "GLOBAL");

        when(sesClient.sendEmail(any())).thenReturn(new SendEmailResult().withMessageId("123456"));

        EmailSendResult result = emailer.sendTemplate("welcome", "bob@example.com", "Bob Smith", ImmutableMap.of("var", "foo"));
        assertEquals("123456", result.getMessageId());
    }
}