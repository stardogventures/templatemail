package io.stardog.email.emailers;

import com.github.jknack.handlebars.Template;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;
import io.stardog.email.interfaces.RawEmailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHandlebarsTemplateEmailer extends AbstractTemplateEmailer implements RawEmailer {
    private final Map<String,HandlebarsEmailTemplate> templates = new HashMap<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractHandlebarsTemplateEmailer.class);

    public void addTemplate(HandlebarsEmailTemplate template) {
        templates.put(template.getTemplateName(), template);
    }

    @Override
    public EmailSendResult sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars) {
        HandlebarsEmailTemplate et = templates.get(templateName);
        if (et == null) {
            LOGGER.error("Template not found: " + templateName);
            throw new IllegalArgumentException("Template not found: " + templateName);
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

        return sendEmail(toEmail, toName, fromEmail, fromName, subject, contentHtml, contentText, templateName);
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
            return toName + " <" + toEmail + ">";
        }
    }
}
