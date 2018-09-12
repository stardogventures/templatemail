package io.stardog.email.emailers;

import com.sun.mail.smtp.SMTPTransport;
import io.stardog.email.data.EmailSendRequest;
import io.stardog.email.data.EmailSendResult;
import io.stardog.email.exceptions.MailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class SmtpEmailer extends AbstractHandlebarsTemplateEmailer {
    private final Properties properties;

    public SmtpEmailer(String smtpHost, int port) {
        this.properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", port);
    }

    public SmtpEmailer(Properties properties) {
        this.properties = properties;
    }

    @Override
    public EmailSendResult sendEmail(EmailSendRequest request) {
        try {
            Session session = Session.getInstance(properties);
            MimeMessage message = new MimeMessage(session);

            InternetAddress to = new InternetAddress(request.getToEmail(), request.getToName().orElse(null));
            InternetAddress from = new InternetAddress(request.getFromEmail(), request.getFromName().orElse(null));
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            if (request.getReplyToEmail().isPresent()) {
                message.setReplyTo(new Address[]{new InternetAddress(request.getReplyToEmail().get())});
            }
            message.setSubject(request.getSubject());
            if (request.getContentHtml().isPresent() && request.getContentText().isPresent()) {
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(request.getContentText().get(), "utf-8");

                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(request.getContentHtml().get(), "text/html; charset=utf-8");

                Multipart multiPart = new MimeMultipart("alternative");
                multiPart.addBodyPart(textPart);
                multiPart.addBodyPart(htmlPart);

                message.setContent(multiPart);
            } else if (request.getContentHtml().isPresent()) {
                message.setContent(request.getContentHtml().get(), "text/html; charset=utf-8");
            } else if (request.getContentText().isPresent()) {
                message.setText(request.getContentText().get(), "utf-8");
            }

            SMTPTransport transport = (SMTPTransport)session.getTransport("smtp");
            transport.connect();
            transport.sendMessage(message, new Address[]{to});
            transport.close();

        } catch (MessagingException e) {
            throw new MailException(e);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException(e);
        }

        return null;
    }
}
