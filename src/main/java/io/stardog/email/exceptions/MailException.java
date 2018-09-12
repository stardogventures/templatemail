package io.stardog.email.exceptions;

public class MailException extends RuntimeException {
    public MailException(Throwable cause) {
        super(cause);
    }
}
