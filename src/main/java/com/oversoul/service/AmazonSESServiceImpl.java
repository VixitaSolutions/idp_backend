package com.oversoul.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;


@Service
public class AmazonSESServiceImpl implements AmazonSESService {

    // Replace sender@example.com with your "From" address.
    // This address must be verified.
    static final String FROM = "Idptoolvixita1@gmail.com";
    static final String FROMNAME = "IDP";

    // Replace recipient@example.com with a "To" address. If your account
    // is still in the sandbox, this address must be verified.
    static final String TO = "sairamm947@gmail.com";
    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    // See https://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html#region-endpoints
    // for more information.
    static final String HOST = "email-smtp.ap-south-1.amazonaws.com";
    // The port you will connect to on the Amazon SES SMTP endpoint.
    static final int PORT = 465;
    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";
    static final String BODY = "Hi Team This is testing " + UUID.randomUUID();
    // Replace smtp_username with your Amazon SES SMTP user name.
    @Value("SMTP_USERNAME")
    private String SMTP_USERNAME;
    // Replace smtp_password with your Amazon SES SMTP password.
    @Value("SMTP_PASSWORD")
    private String SMTP_PASSWORD;

    @Override
    public String sendMail() throws Exception {

        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getDefaultInstance(props);

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html");

        // Create a transport.
        Transport transport = session.getTransport();

        // Send the message.
        try {
            System.out.println("Sending...");

            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);

            // Send the email.
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
            return "success";
        } catch (Exception ex) {
            System.out.println("The email was not sent.");
            System.out.println("Error message: " + ex.getMessage());
            return ex.getMessage();
        } finally {
            // Close and terminate the connection.
            transport.close();
        }
    }
}