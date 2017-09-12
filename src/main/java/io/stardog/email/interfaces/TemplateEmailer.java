package io.stardog.email.interfaces;

import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.HandlebarsEmailTemplate;

import java.util.Collection;
import java.util.Map;

/**
 * An TemplateEmailer is capable of sending emails by the name of that template.
 */
public interface TemplateEmailer {
    EmailSendResult sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars);
    void addGlobalVar(String key, Object val);
    void setWhitelist(Collection<String> whitelist);
}
