package io.stardog.email.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.auto.value.AutoValue;

/*
An object that holds result data from a successful email send.
*/
@AutoValue
@JsonDeserialize(builder=AutoValue_EmailSendResult.Builder.class)
public abstract class EmailSendResult {
    public abstract String getMessageId();

    public static EmailSendResult.Builder builder() {
        return new AutoValue_EmailSendResult.Builder();
    }

    @AutoValue.Builder
    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class Builder {
        public abstract Builder messageId(String messageId);
        public abstract EmailSendResult build();
    }
}
