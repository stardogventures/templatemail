package io.stardog.email.interfaces;

/**
 * An RawEmailer is capable of sending raw emails.
 */
public interface RawEmailer {
    String sendEmail(String toEmail, String toName, String fromEmail, String fromName,
                     String subject, String contentHtml, String contentText);
}
