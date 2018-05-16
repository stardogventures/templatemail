package io.stardog.email.emailers;

import com.sailthru.client.SailthruClient;
import com.sailthru.client.handler.response.JsonResponse;
import com.sailthru.client.params.Send;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;
import io.stardog.email.data.TemplateSendRequest;
import io.stardog.email.interfaces.EmailTemplate;
import io.stardog.email.interfaces.TemplateEmailer;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SailthruEmailer extends AbstractTemplateEmailer {
    private final SailthruClient client;
    private final static Logger LOGGER = LoggerFactory.getLogger(SailthruEmailer.class);

    @Inject
    public SailthruEmailer(SailthruClient client) {
        this.client = client;
    }


    @Override
    public EmailSendResult sendTemplate(TemplateSendRequest request) {
        String templateName = request.getTemplateName();
        String toEmail = request.getToEmail();
        String toName = request.getToName().orElse(null);
        Map<String,Object> vars = request.getVars();
        if (!isWhitelisted(toEmail)) {
            LOGGER.info("Skipping send of " + templateName + " to " + toEmail + " (not whitelisted)");
            return EmailSendResult.builder().messageId("unsent-" + RandomStringUtils.randomAlphanumeric(24)).build();
        }

        Send send = new Send();
        send.setTemplate(templateName);
        send.setEmail(toEmail);

        Map<String,Object> scope = new HashMap<>();
        scope.putAll(getGlobalVars());
        scope.putAll(vars);
        if (toName != null) {
            scope.put("name", toName);
        }
        send.setVars(scope);

        try {
            String sendId = client.send(send).getResponse().get("send_id").toString();
            LOGGER.info("Sent Sailthru " + templateName + " email to " + toEmail + " with id " + sendId);
            return EmailSendResult.builder().messageId(sendId).build();
        } catch (IOException e) {
            LOGGER.error("Failed to send " + templateName + " to " + toEmail, e);
            throw new UncheckedIOException(e);
        }
    }
}
