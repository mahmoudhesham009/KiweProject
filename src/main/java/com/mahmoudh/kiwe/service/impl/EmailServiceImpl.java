package com.mahmoudh.kiwe.service.impl;

import com.mahmoudh.kiwe.dto.Message;
import com.mahmoudh.kiwe.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${email.address}")
    private String fromEmail;
    @Value("${email.password}")
    private String emailPassword;

    @Override
    public Message sendEmailWithNewPassword(String newPassword, String toEmail) {
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(fromEmail, emailPassword);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(fromEmail));

            // Set To: header field of the header.
            message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toEmail));

            // Set Subject: header field
            message.setSubject("You password had been changed");

            // Now set the actual message
            message.setText("you password had been changed to "+newPassword);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

            return new Message("Your Password successfully Changed, check your email: "+toEmail+ " to get The new one");

        } catch (MessagingException mex) {
            mex.printStackTrace();
            return new Message("failed to send Email");
        }


    }
}
