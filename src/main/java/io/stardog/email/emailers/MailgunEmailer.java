package io.stardog.email.emailers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.stardog.email.data.EmailSendResult;
import net.sargue.mailgun.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MailgunEmailer extends AbstractHandlebarsTemplateEmailer {
    private final Configuration config;
    private final static ObjectMapper MAPPER = new ObjectMapper();
    private final static Logger LOGGER = LoggerFactory.getLogger(MailgunEmailer.class);

    @Inject
    public MailgunEmailer(Configuration config) {
        this.config = config;
    }

    @Override
    public EmailSendResult sendEmail(String toEmail, String toName, String fromEmail, String fromName, String subject,
                                     String contentHtml, String contentText, String templateName) {
        Mail mail = buildMail(toEmail, toName, fromEmail, fromName, subject, contentHtml, contentText);
        return sendMail(mail, toEmail, templateName);
    }

    protected Mail buildMail(String toEmail, String toName, String fromEmail, String fromName, String subject,
                             String contentHtml, String contentText) {
        MailBuilder builder = Mail.using(config);

        if (fromName != null) {
            builder.from(fromName, fromEmail);
        } else {
            builder.from(fromEmail);
        }

        if (toName != null) {
            builder.to(toName, toEmail);
        } else {
            builder.to(toEmail);
        }

        builder.subject(subject);

        if (contentHtml != null) {
            builder.html(contentHtml);
        }
        if (contentText != null) {
            builder.text(contentText);
        }
        return builder.build();
    }

    protected EmailSendResult sendMail(Mail mail, String toEmail, String templateName) {
        Response response;
        try {
            response = mail.send();
        } catch (RuntimeException e) {
            LOGGER.error("Unable to send email via Mailgun: ", e);
            throw e;
        }

        if (response.responseCode() < 200 || response.responseCode() >= 300) {
            LOGGER.error("Received non-200 response from Mailgun: " + response.responseCode() + " " + response.responseMessage());
            throw new MailgunException(response.responseCode() + " " + response.responseMessage());
        }

        Map<String,Object> responseMap;
        try {
            responseMap = MAPPER.readValue(response.responseMessage(),
                    new TypeReference<HashMap<String, Object>>() {
                    });
        } catch (IOException e) {
            LOGGER.error("Unable to deserialize response from Mailgun: " + response.responseMessage());
            throw new MailgunException(e);
        }

        if (!responseMap.containsKey("id")) {
            LOGGER.warn("Unexpected response from Mailgun sending " + templateName + " to " + toEmail + " (" + response.responseCode() + ") " + response.responseMessage());
            throw new MailgunException("Unexpected response from Mailgun " + response.responseCode());
        }

        String messageId = responseMap.get("id").toString();
        LOGGER.info("Sent Mailgun email " + templateName + " email to " + toEmail + " with id " + messageId);
        return EmailSendResult.builder().messageId(messageId).build();
    }
}
