package io.stardog.email.emailers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import net.sargue.mailgun.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
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
    public EmailSendResult sendEmail(EmailSendRequest request) {
        Mail mail = buildMail(request);
        return sendMail(mail, request.getToEmail(), request.getTemplateName().orElse("no-template"));
    }

    protected Mail buildMail(EmailSendRequest request) {
        MailBuilder builder = Mail.using(config);

        if (request.getFromName().isPresent()) {
            builder.from(request.getFromName().get(), request.getFromEmail());
        } else {
            builder.from(request.getFromEmail());
        }

        if (request.getToName().isPresent()) {
            builder.to(request.getToName().get(), request.getToEmail());
        } else {
            builder.to(request.getToEmail());
        }

        builder.subject(request.getSubject());

        request.getContentHtml().ifPresent(builder::html);
        request.getContentHtml().ifPresent(builder::text);

        request.getReplyToEmail().ifPresent(builder::replyTo);

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
