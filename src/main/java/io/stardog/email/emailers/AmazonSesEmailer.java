package io.stardog.email.emailers;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
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
    public EmailSendResult sendEmail(String toEmail, String toName, String fromEmail, String fromName,
                            String subject, String contentHtml, String contentText, String templateName) {

        Destination destination = new Destination()
                .withToAddresses(new String[]{toAddress(toEmail, toName)});

        Content subjectContent = new Content().withData(subject);

        Body body = new Body();
        if (contentHtml != null) {
            body = body.withHtml(new Content().withData(contentHtml));
        }
        if (contentText != null) {
            body = body.withText(new Content().withData(contentText));
        }
        Message message = new Message().withSubject(subjectContent).withBody(body);

        SendEmailRequest request = new SendEmailRequest()
                .withSource(toAddress(fromEmail, fromName))
                .withDestination(destination)
                .withMessage(message);

        try {
            String messageId = client.sendEmail(request).getMessageId();
            LOGGER.info("Sent SES " + templateName + " email to " + toEmail + " with id " + messageId);
            return EmailSendResult.builder().messageId(messageId).build();
        } catch (RuntimeException e) {
            LOGGER.error("Unable to send email via SES: ", e);
            throw e;
        }
    }
}
