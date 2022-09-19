package com.example.myCinema.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.myCinema.CheckEntity;

import lombok.AllArgsConstructor;


/**
 * Contains email sending method with html file as email.
 */
@Service
@AllArgsConstructor
public class MailService extends CheckEntity {

    private final JavaMailSender javaMailSender;

    
    /**
     * Sets subject sender, target adress and text of email. 
     * Sends actual email.
     * 
     * @param to target email adress.
     * @param attachment path to attachment May be null if no attachment is needed.
     * @param email content in html form.
     */
    @Async
    public void send(String to, File attachment, String email) {

        try {
            // creating mimeMessage to send with javaMailSender
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            
            // setting mail destination
            mimeMessageHelper.setTo(to);
            // setting subject
            mimeMessageHelper.setSubject("myCinema | Confirm your account.");
            // setting mail adress from sender
            mimeMessageHelper.setFrom("myCinema@gmail.com");
            // setting actual content
            mimeMessageHelper.setText(email, true);
            // setting attachment if not null
            if (attachment != null) 
                mimeMessageHelper.addAttachment(attachment.getName(), attachment);

            // send mail
            javaMailSender.send(mailMessage);

        } catch(MessagingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }


    /**
     * Combines the other {@link #send(String, File, String)} method with the formatting methods (all from mailService).
     * 
     * @param path with html file with email content.
     * @param fillInList list with variables that should replace '%s' symbols using String.formatted().
     * @param to target email adress.
     * @param attachment path to attachment. May be null if no attachment is needed.
     */
    public void send(String path, List<String> fillInList, String to, File attachment) {

        // getting email from html file
        String email;
        try {
            email = readHtmlToString(path);
            
        } catch (IOException e) {
            System.out.println("ioException");
            throw new IllegalStateException(e.getMessage());

        } catch (InterruptedException e) {
            System.out.println("interruptedException");
            throw new IllegalStateException(e.getMessage());

        } catch (NullPointerException e) {
            System.out.println("nullPointerException");
            throw new IllegalStateException(e.getMessage());
        }

        // formatting email with fillers from fillInList
        email = formatString(email, fillInList);
        
        // sending email
        send(to, attachment, email);
    }
    

    /**
     * Reads email text form html file. Adds username and confirmation token.
     * 
     * @param emailPath from which the html file comes from.
     * @param name of appUser.
     * @param token confirmation token of appUser to confirm.
     * @throws IOException
     * @throws InterruptedException
     */
    public String readHtmlToString(String path) throws IOException, InterruptedException {
        
        // checking path
        if (objectNullOrEmpty(path)) 
            throw new IllegalStateException("Path of html file can neither be null nor empty.");
        
        // streaming and reading html file to a String
        try (InputStream is = getClass().getResourceAsStream(path); 
             InputStreamReader isr = new InputStreamReader(is); 
             BufferedReader br = new BufferedReader(isr)) {

            return br.lines().collect(Collectors.joining());
        }
    }


    /**
     * Formats any (large) String that contains '%s' symbols using filler Strings from the fillInList.
     * 
     * @param str the String to format.
     * @param fillInList List with filler Strings to replace the '%s' symbols.
     * @return the formatted String.
     */
    public String formatString(String str, List<String> fillInList) {

        // checking str and fillInList
        if (objectNullOrEmpty(str) || iterableNullOrEmpty(fillInList)) 
            throw new IllegalStateException("Either String to format or List with fillers is null or empty.");

        // getting list of substrings to format
        List<String> subStrings = splitStringForFormatting(str, fillInList);
        
        String strFormatted = "";

        // concatenating formatted substrings
        for (String subString : subStrings) {
            strFormatted += subString;
        }
        
        return strFormatted;
    }


//// helper functions


    /**
     * Splits a String with '%s' symbols which can be used for String.formatted() into substrings. Each subString
     * ends with a '%s' and is added to an ArrayList.
     * 
     * <p> If the string does not end on a '%s' the last subString is also added to the list.
     * 
     * <p> E.g. 
     * <p> {@code String str = "My name is %s and I am %s years old.";} 
     * 
     * <p> would become a list with 3 elements:
     * <p> {@code [My name is %s,  and I am %s,  years old.]} 
     * 
     * <p> which would be formatted and could be returned like so:
     * <p> {@code [My name is Max,  and I am 30,  years old.]}
     * 
     * @param email
     * @param fillInList
     * @return
     */
    private List<String> splitStringForFormatting(String str, List<String> fillInList) {

        List<String> subStrings = new ArrayList<>();

        for (String filler : fillInList) {
            // finding index of %s
            int idx = str.indexOf("%s") + 2;
                            
            // getting a substring ending with the found "%s"
            String subString = str.substring(0, idx);
            
            // formatting substring
            subString = subString.formatted(filler);
            
            // adding formatted substring to subStrings list
            subStrings.add(subString);

            // cutting substring from str
            str = str.substring(idx);
        }

        // adding anything after the last %s to the subStrings list
        subStrings.add(str);

        return subStrings;
    }
}