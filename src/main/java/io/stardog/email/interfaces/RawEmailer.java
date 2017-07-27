package io.stardog.email.interfaces;

import io.stardog.email.data.EmailSendResult;

/**
 * An RawEmailer is capable of sending raw emails.
 */
public interface RawEmailer {
    EmailSendResult sendEmail(String toEmail, String toName, String fromEmail, String fromName,
                     String subject, String contentHtml, String contentText, String templateName);
}
