package io.stardog.email.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

import java.util.Map;
import java.util.Optional;

@AutoValue
@JsonDeserialize(builder=AutoValue_TemplateSendRequest.Builder.class)
public abstract class TemplateSendRequest {
    public abstract String getTemplateName();
    public abstract String getToEmail();
    public abstract Optional<String> getToName();
    public abstract Map<String,Object> getVars();
    public abstract Optional<String> getReplyToEmail();

    public static TemplateSendRequest.Builder builder() {
        return new AutoValue_TemplateSendRequest.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder templateName(String name);
        public abstract Builder toEmail(String email);
        public abstract Builder toName(String name);
        public abstract Builder vars(Map<String,Object> vars);
        public abstract Builder replyToEmail(String email);
        public abstract TemplateSendRequest build();
    }
}
