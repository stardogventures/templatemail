package io.stardog.email.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
@JsonDeserialize(builder=AutoValue_EmailSendRequest.Builder.class)
public abstract class EmailSendRequest {
    public abstract Optional<String> getFromName();
    public abstract String getFromEmail();
    public abstract Optional<String> getToName();
    public abstract String getToEmail();
    public abstract String getSubject();
    public abstract Optional<String> getContentHtml();
    public abstract Optional<String> getContentText();
    public abstract Optional<String> getReplyToEmail();
    public abstract Optional<String> getTemplateName();

    public static EmailSendRequest.Builder builder() {
        return new AutoValue_EmailSendRequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder fromName(String name);
        public abstract Builder fromEmail(String email);
        public abstract Builder toName(String name);
        public abstract Builder toEmail(String email);
        public abstract Builder subject(String subject);
        public abstract Builder contentHtml(String html);
        public abstract Builder contentText(String text);
        public abstract Builder replyToEmail(String email);
        public abstract Builder templateName(String name);
        public abstract EmailSendRequest build();
    }
}
