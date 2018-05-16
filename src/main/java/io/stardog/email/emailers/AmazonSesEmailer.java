package io.stardog.email.emailers;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.google.common.collect.ImmutableList;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmazonSesEmailer extends AbstractHandlebarsTemplateEmailer {
    private final AmazonSimpleEmailServiceClient client;
    private final static Logger LOGGER = LoggerFactory.getLogger(AmazonSesEmailer.class);

    @Inject
    public AmazonSesEmailer(AmazonSimpleEmailServiceClient client) {
        super();
        this.client = client;
    }

    @Override
    public EmailSendResult sendEmail(EmailSendRequest request) {
        String toEmail = request.getToEmail();
        String toName = request.getToName().orElse(toEmail);
        String fromEmail = request.getFromEmail();
        String fromName = request.getFromName().orElse(fromEmail);
        String subject = request.getSubject();

        Destination destination = new Destination()
                .withToAddresses(ImmutableList.of(toAddress(toEmail, toName)));

        Content subjectContent = new Content().withData(subject);

        final Body body = new Body();
        request.getContentHtml().ifPresent(
                html -> body.withHtml(new Content().withData(html)));
        request.getContentText().ifPresent(
                text -> body.withText(new Content().withData(text)));
        Message message = new Message().withSubject(subjectContent).withBody(body);

        SendEmailRequest sesRequest = new SendEmailRequest()
                .withSource(toAddress(fromEmail, fromName))
                .withDestination(destination)
                .withMessage(message);
        request.getReplyToEmail().ifPresent(
                replyTo -> sesRequest.withReplyToAddresses(ImmutableList.of(replyTo)));

        try {
            String messageId = client.sendEmail(sesRequest).getMessageId();
            LOGGER.info("Sent SES " + request.getTemplateName().orElse("unknown") + " email to " + toEmail + " with id " + messageId);
            return EmailSendResult.builder().messageId(messageId).build();
        } catch (RuntimeException e) {
            LOGGER.error("Unable to send email via SES: ", e);
            throw e;
        }
    }
}
