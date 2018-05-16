package io.stardog.email.emailers;

import com.github.jknack.handlebars.Template;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;
import io.stardog.email.data.TemplateSendRequest;
import io.stardog.email.interfaces.EmailTemplate;
import io.stardog.email.interfaces.RawEmailer;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHandlebarsTemplateEmailer extends AbstractTemplateEmailer implements RawEmailer {
    private final Map<String,EmailTemplate<Template>> templates = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractHandlebarsTemplateEmailer.class);

    public void addTemplate(EmailTemplate<Template> template) {
        templates.put(template.getName(), template);
    }

    @Override
    public EmailSendResult sendTemplate(TemplateSendRequest request) {
        String templateName = request.getTemplateName();
        String toEmail = request.getToEmail();
        Map<String,Object> vars = request.getVars();
        String toName = request.getToName().orElse(null);

        EmailTemplate<Template> et = templates.get(templateName);
        if (et == null) {
            LOGGER.error("Template not found: " + templateName);
            throw new IllegalArgumentException("Template not found: " + templateName);
        }

        if (!isWhitelisted(toEmail)) {
            LOGGER.info("Skipping send of " + templateName + " to " + toEmail + " (not whitelisted)");
            return EmailSendResult.builder().messageId("unsent-" + RandomStringUtils.randomAlphanumeric(24)).build();
        }

        Map<String,Object> scope = new HashMap<>();
        scope.putAll(getGlobalVars());
        scope.putAll(vars);
        scope.put("email", toEmail);
        if (toName != null) {
            scope.put("name", toName);
        }

        String fromName = evaluateHandlebarsTemplate(et.getFromName(), scope);
        String fromEmail = evaluateHandlebarsTemplate(et.getFromEmail(), scope);
        String subject = evaluateHandlebarsTemplate(et.getSubject(), scope);
        String contentHtml = evaluateHandlebarsTemplate(et.getContentHtml(), scope);
        String contentText = evaluateHandlebarsTemplate(et.getContentText(), scope);

        EmailSendRequest.Builder builder = EmailSendRequest.builder()
                .templateName(templateName)
                .toEmail(toEmail)
                .toName(toName)
                .fromName(fromName)
                .fromEmail(fromEmail)
                .subject(subject);
        if (contentHtml != null) {
            builder.contentHtml(contentHtml);
        }
        if (contentText != null) {
            builder.contentText(contentText);
        }
        request.getReplyToEmail().ifPresent(builder::replyToEmail);

        return sendEmail(builder.build());
    }

    protected String evaluateHandlebarsTemplate(Template template, Map<String,Object> context) {
        if (template == null) {
            return null;
        }
        try {
            return template.apply(context);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String toAddress(String toEmail, String toName) {
        if (toName == null) {
            return toEmail;
        } else {
            try {
                return new InternetAddress(toEmail, toName).toString();
            } catch (UnsupportedEncodingException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
