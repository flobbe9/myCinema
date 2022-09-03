package com.example.myCinema.mail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    
    @Async
    public void send(String to, String email) {

        try {
            // creating mimeMessage to send with javaMailSender
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, "utf-8");
            
            // setting mail destination
            mimeMessageHelper.setTo(to);
            // setting subject
            mimeMessageHelper.setSubject("myCinema | Confirm your account.");
            // setting mail adress from sender
            mimeMessageHelper.setFrom("schikarski98@gmail.com");
            // setting actual content
            mimeMessageHelper.setText(email, true);

            // send mail
            javaMailSender.send(mailMessage);

        } catch(MessagingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }


    public String createEmail(Path emailPath, String name, String token) {

        try {
            // reading mail content from html file from emailPath
            return Files.readString(emailPath).formatted(name, token);
            
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}