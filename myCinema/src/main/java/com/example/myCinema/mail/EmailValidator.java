package com.example.myCinema.mail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;


/**
 * Checks email string with regex for correct syntax.
 * 
 * <p>Helper methods throw an exception if check was unsuccessful but are void methods.
 */
@Service
public class EmailValidator {

    /** Counts '@' symbols in various methods. */
    private int atSymbolCount = 0;

    /** Index of '@' symbol so it's not missplaced. */
    private int atSymbolIndx = -1;

    /** Index of '.' symbols so they are not missplaced. */
    private int dotSymbolIndx = 1;

    /** Max number of characters an email adress can contain. */
    private static final int MAX_EMAIL_LENGTH = 253;


    /**
     * Calls all helper methods to validate email. Each helper will throw an exception if check is unsuccessful.
     * 
     * @param email to check.
     * @return true if email is valid.
     */
    public Boolean validate(String email) {

        checkNull(email);
        
        checkLength(email);

        checkWhiteSpace(email);

        checkFirstCharacter(email);

        setAtAndDotIndx(email);

        checkSequenceAfterAt(email);

        checkEnd(email);

        return true;
    }


    /** Email cannot be null. */
    private void checkNull(String email) {

        if (email == null) 
            throw new IllegalStateException("Email cannot be null.");
    }


    /** Email cannot be too long. */
    private void checkLength(String email) {

        if (email.length() > MAX_EMAIL_LENGTH) 
            throw new IllegalStateException("Invalid email. Email too long.");
    }


    /** Email cannot contain white space. */
    private void checkWhiteSpace(String email) {

        Pattern pattern = Pattern.compile("\s");
        Matcher whiteSpace = pattern.matcher(email);

        if (whiteSpace.find()) 
            throw new IllegalStateException("Invalid email. Remove white space.");
    }


    /** First character must be alphabetical or numeric. */
    private void checkFirstCharacter(String email) {

        Pattern pattern = Pattern.compile("^[abcdefghijklmnopqrstuvwxyz0-9]", Pattern.CASE_INSENSITIVE);
        Matcher firstChar = pattern.matcher(email);

        if (!firstChar.find())
            throw new IllegalStateException("Invalid email. Email cannot start with a symbol.");
    }


    /** 
     * Counting '@' symbols and marking '@' and '.' index in the email. 
     * 
     * <p>There can only be one '@'.
     * <p>There has to at least one '.'.
     * <p>The first '.' symbol has to be after '@'.
     */
    private void setAtAndDotIndx(String email) {

        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                atSymbolCount++;
                atSymbolIndx = i;
            }
            if (email.charAt(i) == '.') {
                dotSymbolIndx = i;
            }
        }

        if (atSymbolIndx == -1) 
            throw new IllegalStateException("Invalid email. Missing '@'.");
        
        if (atSymbolCount != 1) 
            throw new IllegalStateException("Invalid email. Cannot use '@' more than once.");
        
        if (dotSymbolIndx == -1 || dotSymbolIndx < atSymbolIndx) 
            throw new IllegalStateException("Invalid email. Placed '.' wrong or missed it.");
    }
    
    
    /** Domain can only contain of alphabetical characters and has to come after '@' and before last '.'. */
    private void checkSequenceAfterAt(String email) {

        Pattern pattern = Pattern.compile("[^abcdefghijklmnopqrstuvwxyz]");
        String afterAtStr = email.substring(atSymbolIndx + 1, dotSymbolIndx);
        Matcher afterAtMatcher = pattern.matcher(afterAtStr);

        if (afterAtMatcher.find() || afterAtStr.equals("")) 
            throw new IllegalStateException("Invalid email.");
    }


    /** Last sequence can only contain alphabetical characters. */
    private void checkEnd(String email) {
        
        Pattern pattern = Pattern.compile("[^abcdefghijklmnopqrstuvwxyz]");
        String afterDotStr = email.substring(dotSymbolIndx + 1);
        Matcher afterDotMatcher = pattern.matcher(afterDotStr);

        if (afterDotMatcher.find() || afterDotStr.equals("")) 
            throw new IllegalStateException("Invalid email.");
    }
}
