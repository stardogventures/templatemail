package io.stardog.email.interfaces;

import java.util.Map;

/**
 * An TemplateEmailer is capable of sending emails by the name of that template.
 *  Any specifics involving adding templates, etc, are specific to the name of that implementation.
 */
public interface TemplateEmailer {
    public String sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars);
    public void putGlobalVar(String key, Object val);
}
