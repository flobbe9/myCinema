package com.example.myCinema.mail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;


/**
 * Contains email sending method with html file as email.
 */
@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    
    /**
     * Sets subject sender, target adress and text of email. Sends actual email.
     * 
     * @param to target email adress.
     * @param email text in html form.
     */
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
            mimeMessageHelper.setFrom("myCinema@gmail.com");
            // setting actual content
            mimeMessageHelper.setText(email, true);

            // send mail
            javaMailSender.send(mailMessage);

        } catch(MessagingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
 
    
    /**
     * Reads email text form html file. Adds username and confirmation token.
     * 
     * @param emailPath from which the html file comes from.
     * @param name of appUser.
     * @param token confirmation token of appUser to confirm.
     * @throws IOException
     */
    public String createConfirmationEmail(String path, String name, String token) {
        // streaming html file 
        InputStream is = getClass().getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        // reading stream 
        String email = br.lines().collect(Collectors.joining());

        return email.formatted(name, token);
    }
}