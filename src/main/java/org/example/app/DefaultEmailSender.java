package org.example.app;

public class DefaultEmailSender implements EmailSender {

    @Override
    public void send(String to, String subject, String body) {
        System.out.printf(
                "Sending email to %s. Subject: %s. Body: %s\n",
                to,
                subject,
                body
        );
    }
}
