package io.stardog.email.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.auto.value.AutoValue;
import io.stardog.email.interfaces.EmailTemplate;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Scanner;

/*
An object that holds prepared Handlebars templates for each of the fields in the email template.
*/
@AutoValue
@JsonDeserialize(builder=AutoValue_HandlebarsEmailTemplate.Builder.class)
public abstract class HandlebarsEmailTemplate implements EmailTemplate<Template> {
    public abstract String getName();

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
        private static Handlebars HANDLEBARS_HTML = new Handlebars();
        private static Handlebars HANDLEBARS_TEXT = new Handlebars()
                .with(EscapingStrategy.NOOP);

        public abstract Builder name(String templateName);

        public abstract Builder fromName(Template fromName);
        public abstract Builder fromEmail(Template fromEmail);
        public abstract Builder subject(Template subject);
        public abstract Builder contentHtml(Template contentHtml);
        public abstract Builder contentText(Template contentText);

        public Builder fromName(String fromName) {
            try {
                return fromName(HANDLEBARS_TEXT.compileInline(fromName));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Builder fromName(File file) {
            try {
                return fromName(new Scanner(file).useDelimiter("\\Z").next());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Builder fromEmail(String fromEmail) {
            try {
                return fromEmail(HANDLEBARS_TEXT.compileInline(fromEmail));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        public Builder fromEmail(File file) {
            try {
                return fromEmail(new Scanner(file).useDelimiter("\\Z").next());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Builder subject(String subject) {
            try {
                return subject(HANDLEBARS_TEXT.compileInline(subject));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        public Builder subject(File file) {
            try {
                return subject(new Scanner(file).useDelimiter("\\Z").next());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Builder contentHtml(String contentHtml) {
            try {
                return contentHtml(HANDLEBARS_HTML.compileInline(contentHtml));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        public Builder contentHtml(File file) {
            try {
                return contentHtml(new Scanner(file).useDelimiter("\\Z").next());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public Builder contentText(String contentText) {
            try {
                return contentText(HANDLEBARS_TEXT.compileInline(contentText));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        public Builder contentText(File file) {
            try {
                return contentText(new Scanner(file).useDelimiter("\\Z").next());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public abstract HandlebarsEmailTemplate build();
    }
}
