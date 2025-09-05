package org.example.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesEmailService {
    private final SesClient sesClient;
    private final String FROM_EMAIL = "manali.joshi05@gmail.com"; // Change to your verified SES sender
    private final String TO_EMAIL = "manali.joshi05@gmail.com";   // Change to your fixed recipient

    public SesEmailService() {
        this.sesClient = SesClient.builder()
                .region(Region.EU_NORTH_1) // Change to your SES region
                .build();
    }

    public void sendOrderEmail(String customerEmail, String subject, String body) {
        Destination destination = Destination.builder()
                .toAddresses(TO_EMAIL)
                .ccAddresses(customerEmail)
                .build();

        Content contentSubject = Content.builder().data(subject).build();
        Content contentBody = Content.builder().data(body).build();
        Body emailBody = Body.builder().text(contentBody).build();
        Message message = Message.builder().subject(contentSubject).body(emailBody).build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(FROM_EMAIL)
                .build();

        sesClient.sendEmail(request);
    }
}

