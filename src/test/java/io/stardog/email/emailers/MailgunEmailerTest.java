package io.stardog.email.emailers;

import io.stardog.email.data.EmailSendResult;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import net.sargue.mailgun.Response;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MailgunEmailerTest {
    private MailgunEmailer emailer;

    @Before
    public void setUp() throws Exception {
        Configuration config = new Configuration();
        emailer = new MailgunEmailer(config);
    }

    @Test
    public void sendMail() throws Exception {
        Mail mail = mock(Mail.class);
        Response response = mock(net.sargue.mailgun.Response.class);
        when(response.responseCode()).thenReturn(200);
        when(response.responseMessage()).thenReturn("{\"id\":\"123456\"}");
        when(mail.send()).thenReturn(response);

        EmailSendResult result = emailer.sendMail(mail, "example@example.com", "test-template");
        assertEquals("123456", result.getMessageId());
    }
}