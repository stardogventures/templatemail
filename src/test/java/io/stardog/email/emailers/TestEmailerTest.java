package io.stardog.email.emailers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;
import io.stardog.email.data.TemplateSendRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class TestEmailerTest {
    TestEmailer emailer;

    @Before
    public void setUp() throws Exception {
        emailer = new TestEmailer();
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

        emailer.sendTemplate("welcome", "bob@example.com", "Bob Smith", ImmutableMap.of("var", "foo"));

        TemplateSendRequest lastSend = emailer.getLastTemplateSend();
        assertEquals("welcome", lastSend.getTemplateName());
        assertEquals("bob@example.com", lastSend.getToEmail());
        assertEquals("Bob Smith", lastSend.getToName().get());
        assertEquals(ImmutableMap.of("var", "foo"), lastSend.getVars());

        EmailSendRequest lastEmail = emailer.getLastEmailSend();
        assertEquals("Bob Smith, welcome. You signed up with bob@example.com. var=foo global=GLOBAL", lastEmail.getContentHtml().get());

        assertEquals(ImmutableList.of(lastEmail), emailer.getRecentEmailSends());
        assertEquals(ImmutableList.of(lastSend), emailer.getRecentTemplateSends());

        emailer.clear();
        assertEquals(0, emailer.getRecentTemplateSends().size());
        assertEquals(0, emailer.getRecentEmailSends().size());
        assertNull(emailer.getLastEmailSend());
        assertNull(emailer.getLastTemplateSend());
    }
}