package com.sgu.userservice.service.impl;

import com.sgu.userservice.model.EmailDetails;
import com.sgu.userservice.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
// Class
// Implementing EmailService interface
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    // Method 1
    // To send a simple email
    public Boolean sendSimpleMail(EmailDetails details)
    {

        // Try block to check for exceptions
        try {


            MimeMessage message = javaMailSender.createMimeMessage();

            message.setFrom(new InternetAddress(sender));
            message.setRecipients(MimeMessage.RecipientType.TO, details.getRecipient());
            message.setSubject(details.getSubject());

            String htmlContent = details.getMsgBody();
            message.setContent(htmlContent, "text/html; charset=utf-8");

            javaMailSender.send(message);
            return true;
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return false;
        }
    }
}
