package org.example.services;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {
    private final SesClient sesClient;

    public EmailService() {
        this.sesClient = SesClient.create();
    }

    public void sendOrderEmail(String fixedToEmail, String ccEmail, String subject, String body) {
        SesClient sesClient = SesClient.create();
        Destination destination = Destination.builder()
                .toAddresses(fixedToEmail)
                .ccAddresses(ccEmail)
                .build();

        Message message = Message.builder()
                .subject(Content.builder().data(subject).build())
                .body(Body.builder().text(Content.builder().data(body).build()).build())
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(fixedToEmail)
                .build();

        sesClient.sendEmail(request);
    }
}
