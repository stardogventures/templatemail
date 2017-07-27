package io.stardog.email.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/*
An object that holds prepared Handlebars templates for each of the fields in the email template.
*/
@AutoValue
@JsonDeserialize(builder=AutoValue_HandlebarsEmailTemplate.Builder.class)
public abstract class HandlebarsEmailTemplate {
    public abstract String getTemplateName();

    public abstract Template getFromName();

    public abstract Template getFromEmail();

    public abstract Template getSubject();

    @Nullable
    public abstract Template getContentHtml();

    @Nullable
    public abstract Template getContentText();

    public static HandlebarsEmailTemplate.Builder builder() {
        return new AutoValue_HandlebarsEmailTemplate.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        private static Handlebars HANDLEBARS = new Handlebars();

        public abstract Builder templateName(String templateName);

        public abstract Builder fromName(Template fromName);
        public abstract Builder fromEmail(Template fromEmail);
        public abstract Builder subject(Template subject);
        public abstract Builder contentHtml(Template contentHtml);
        public abstract Builder contentText(Template contentText);

        public Builder fromName(String fromName) throws IOException {
            return fromName(HANDLEBARS.compileInline(fromName));
        }
        public Builder fromName(File file) throws IOException { return fromName(new Scanner(file).useDelimiter("\\Z").next()); }

        public Builder fromEmail(String fromEmail) throws IOException {
            return fromEmail(HANDLEBARS.compileInline(fromEmail));
        }
        public Builder fromEmail(File file) throws IOException { return fromEmail(new Scanner(file).useDelimiter("\\Z").next()); }

        public Builder subject(String subject) throws IOException {
            return subject(HANDLEBARS.compileInline(subject));
        }
        public Builder subject(File file) throws IOException { return subject(new Scanner(file).useDelimiter("\\Z").next()); }

        public Builder contentHtml(String contentHtml) throws IOException {
            return contentHtml(HANDLEBARS.compileInline(contentHtml));
        }
        public Builder contentHtml(File file) throws IOException { return contentHtml(new Scanner(file).useDelimiter("\\Z").next()); }

        public Builder contentText(String contentText) throws IOException {
            return contentText(HANDLEBARS.compileInline(contentText));
        }
        public Builder contentText(File file) throws IOException { return contentText(new Scanner(file).useDelimiter("\\Z").next()); }

        public abstract HandlebarsEmailTemplate build();
    }
}
