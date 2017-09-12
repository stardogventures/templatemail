package io.stardog.email.emailers;

import io.stardog.email.data.EmailSendResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;

/**
 * A version of an emailer that does not actually send. Useful for tests or local use.
 */
@Singleton
public class NonEmailer extends AbstractHandlebarsTemplateEmailer {
    private final boolean logFullMessages;
    private final static Logger LOGGER = LoggerFactory.getLogger(NonEmailer.class);

    public NonEmailer() {
        this(false);
    }

    public NonEmailer(boolean logFullMessages) {
        this.logFullMessages = logFullMessages;
    }

    @Override
    public EmailSendResult sendEmail(String toEmail, String toName, String fromEmail, String fromName, String subject, String contentHtml, String contentText, String templateName) {
        if (logFullMessages) {
            StringBuilder sb = new StringBuilder();
            sb.append("To: " + toAddress(toEmail, toName) + "\n");
            sb.append("From: " + toAddress(fromEmail, fromName) + "\n");
            sb.append("Subject: " + subject + "\n");
            if (contentHtml != null) {
                sb.append("\n" + contentHtml);
            }
            if (contentText != null) {
                sb.append("\n" + contentText);
            }
            LOGGER.info(sb.toString());
        }
        String messageId = "unsent-" + RandomStringUtils.randomAlphanumeric(24);
        LOGGER.info("Did not send " + templateName + " email to " + toAddress(toEmail, toName));
        return EmailSendResult.builder().messageId(messageId).build();
    }
}
