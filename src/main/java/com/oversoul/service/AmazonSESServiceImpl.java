package com.oversoul.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.oversoul.exception.CommonException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AmazonSESServiceImpl implements AmazonSESService {


    static final String FROM = "idptoolvixita1@gmail.com";
    // The subject line for the email.
    static final String SUBJECT = "IDP";
    // The email body for recipients with non-HTML email clients.
    static final String TEXTBODY = "This email was sent through Amazon SES "
            + "using the AWS SDK for Java.";
    // Replace smtp_username with your Amazon SES SMTP user name.
    @Value("${SMTP_USERNAME}")
    private String SMTP_USERNAME;
    // Replace smtp_password with your Amazon SES SMTP password.
    @Value("${SMTP_PASSWORD}")
    private String SMTP_PASSWORD;

    @Override
    public String sendMail(String TO, String htmlBody) throws Exception {
        try {
            BasicAWSCredentials awsCreds = new BasicAWSCredentials(SMTP_USERNAME, SMTP_PASSWORD);

            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                            // Replace US_WEST_2 with the AWS Region you're using for
                            // Amazon SES.
                            .withRegion(Regions.AP_SOUTH_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(TO))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(htmlBody))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(TEXTBODY)))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(SUBJECT)))
                    .withSource(FROM);

            client.sendEmail(request);
            System.out.println("Email sent!");
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: "
                    + ex.getMessage());
            throw new CommonException(ex.getMessage());
        }
        return null;
    }
}