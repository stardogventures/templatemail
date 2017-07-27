package io.stardog.email.emailers;

import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.google.common.collect.ImmutableMap;
import com.sailthru.client.SailthruClient;
import com.sailthru.client.handler.response.JsonResponse;
import io.stardog.email.data.EmailSendResult;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SailthruEmailerTest {
    @Test
    public void sendTemplate() throws Exception {
        SailthruClient sailthruClient = mock(SailthruClient.class);
        SailthruEmailer emailer = new SailthruEmailer(sailthruClient);
        emailer.addGlobalVar("globalVar", "GLOBAL");

        when(sailthruClient.send(any())).thenReturn(new JsonResponse(ImmutableMap.of("send_id", "123456")));

        EmailSendResult result = emailer.sendTemplate("welcome", "bob@example.com", "Bob Smith", ImmutableMap.of("var", "foo"));
        assertEquals("123456", result.getMessageId());
    }
}