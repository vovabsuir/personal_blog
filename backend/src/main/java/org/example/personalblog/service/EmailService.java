package org.example.personalblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${GMAIL_ADDRESS}")
    private String sender;

    public void sendSubscriptionEmail(String to, String subject, String url) {
        String text =
                """
                This is a confirmation email to confirm subscription on the Personal Blog website. If you didn't expect
                to receive this message, just ignore it.
                Click on url to confirm subscription:
                """;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text + url);

        mailSender.send(message);
    }

    public void sendMessageEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);

            log.info("Email successfully sent to: {}", to);
        } catch (MailSendException ex) {
            log.error("Failed to send email to: {}. Error: {}", to, ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error while sending email to: {}", to, ex);
        }
    }
}
