package io.stardog.email.interfaces;

public interface EmailTemplate<T> {
    String getName();
    T getFromName();
    T getFromEmail();
    T getSubject();
    T getContentHtml();
    T getContentText();
}
