package io.stardog.email.interfaces;

import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;

/**
 * An RawEmailer is capable of sending raw emails.
 */
public interface RawEmailer {
    EmailSendResult sendEmail(EmailSendRequest request);
}
