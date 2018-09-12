package io.stardog.email.emailers;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.data.TemplateSendRequest;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.Queue;

public class TestEmailer extends AbstractHandlebarsTemplateEmailer {
    private final Queue<TemplateSendRequest> templateSends;
    private final Queue<EmailSendRequest> emailSends;
    private final static int DEFAULT_SIZE = 10;

    public TestEmailer() {
        this(DEFAULT_SIZE);
    }

    public TestEmailer(int maxSize) {
        this.templateSends = EvictingQueue.create(maxSize);
        this.emailSends = EvictingQueue.create(maxSize);
    }

    @Override
    public EmailSendResult sendTemplate(TemplateSendRequest request) {
        templateSends.add(request);
        return super.sendTemplate(request);
    }

    @Override
    public EmailSendResult sendEmail(EmailSendRequest request) {
        emailSends.add(request);
        String messageId = "test-" + RandomStringUtils.randomAlphanumeric(24);
        return EmailSendResult.builder().messageId(messageId).build();
    }

    public List<TemplateSendRequest> getRecentTemplateSends() {
        return ImmutableList.copyOf(templateSends);
    }

    public TemplateSendRequest getLastTemplateSend() {
        return templateSends.peek();
    }

    public List<EmailSendRequest> getRecentEmailSends() {
        return ImmutableList.copyOf(emailSends);
    }

    public EmailSendRequest getLastEmailSend() {
        return emailSends.peek();
    }

    public void clear() {
        templateSends.clear();
        emailSends.clear();
    }
}
