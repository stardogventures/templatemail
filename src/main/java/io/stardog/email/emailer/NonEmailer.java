package io.stardog.email.emailer;

import io.stardog.email.interfaces.EmailTemplateSender;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;

/**
 * A version of an emailer that does nothing. Useful for tests or local use.
 */
public class NonEmailer implements EmailTemplateSender {
    @Override
    public String sendTemplate(String templateName, String toEmail, String toName, Map<String, Object> vars) {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    public void putGlobalVar(String key, Object val) {
    }
}
