package io.stardog.email.emailers;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A version of an emailer that does not actually send. Useful for tests or local use.
 */
public class NonEmailer extends RawTemplateEmailer {
    private final boolean logFullMessages;
    private final static Logger LOGGER = LoggerFactory.getLogger(AmazonSesEmailer.class);

    public NonEmailer() {
        this(false);
    }

    public NonEmailer(boolean logFullMessages) {
        this.logFullMessages = logFullMessages;
    }

    private String toSendName(String email, String name) {
        if (name != null) {
            return name + " <" + email + ">";
        } else {
            return email;
        }
    }

    @Override
    public String sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars) {
        LOGGER.info("Non-sending " + templateName + " to " + toSendName(toEmail, toName));
        return super.sendTemplate(templateName, toEmail, toName, vars);
    }

    @Override
    public String sendEmail(String toEmail, String toName, String fromEmail, String fromName, String subject, String contentHtml, String contentText) {
        if (logFullMessages) {
            StringBuilder sb = new StringBuilder();
            sb.append("To: " + toSendName(toEmail, toName) + "\n");
            sb.append("From: " + toSendName(fromEmail, fromName) + "\n");
            sb.append("Subject: " + subject + "\n");
            if (contentHtml != null) {
                sb.append("\n" + contentHtml);
            }
            if (contentText != null) {
                sb.append("\n" + contentText);
            }
            LOGGER.info(sb.toString());
        }
        return RandomStringUtils.randomAlphanumeric(24);
    }
}
