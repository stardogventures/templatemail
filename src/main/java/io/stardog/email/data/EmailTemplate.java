package io.stardog.email.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.auto.value.AutoValue;
import com.sun.istack.internal.Nullable;

import java.io.StringReader;

/*
An object that holds prepared Mustache templates for each of the fields in the email template.
*/
@AutoValue
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder=AutoValue_EmailTemplate.Builder.class)
public abstract class EmailTemplate {
    public abstract String getTemplateName();

    public abstract String getFromName();

    public abstract String getFromEmail();

    public abstract Mustache getSubject();

    @Nullable
    public abstract Mustache getContentHtml();

    @Nullable
    public abstract Mustache getContentText();

    public static EmailTemplate.Builder builder() {
        return new AutoValue_EmailTemplate.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        private static MustacheFactory MF = new DefaultMustacheFactory();

        public abstract Builder templateName(String templateName);
        public abstract Builder fromName(String fromName);
        public abstract Builder fromEmail(String fromEmail);
        public abstract Builder subject(Mustache subject);
        public abstract Builder contentHtml(Mustache contentHtml);
        public abstract Builder contentText(Mustache contentText);

        public Builder subject(String subject) {
            return subject(MF.compile(new StringReader(subject), subject));
        }

        public Builder contentHtml(String contentHtml) {
            return contentHtml(MF.compile(new StringReader(contentHtml), contentHtml));
        }

        public Builder contentText(String contentText) {
            return contentText(MF.compile(new StringReader(contentText), contentText));
        }

        public abstract EmailTemplate build();
    }
}
