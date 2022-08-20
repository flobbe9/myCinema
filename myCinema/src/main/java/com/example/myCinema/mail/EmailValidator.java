package com.example.myCinema.mail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;


@Service
public class EmailValidator {
    private int atSymbolCount = 0;
    private int atSymbolIndx = -1;
    private int dotSymbolIndx = 1;


    public Boolean validate(String email) {
        checkNull(email);
        
        checkLength(email, 253);

        checkWhiteSpace(email);

        checkFirstCharacter(email);

        setAtAndDotIndx(email);

        checkSequenceAfterAt(email);

        checkEnd(email);

        return true;
    }


    private void checkNull(String email) {
        if (email == null) {
            throw new IllegalStateException("Email cannot be null.");
        }
    }


    private void checkLength(String email, int length) {
        if (email.length() > length) {
            throw new IllegalStateException("Invalid email. Email too long.");
        }
    }


    private void checkWhiteSpace(String email) {
        Pattern pattern = Pattern.compile("\s");
        Matcher whiteSpace = pattern.matcher(email);

        if (whiteSpace.find()) {
            throw new IllegalStateException("Invalid email. Remove white space.");
        }
    }


    private void checkFirstCharacter(String email) {
        Pattern pattern = Pattern.compile("^[abcdefghijklmnopqrstuvwxyz0-9]", Pattern.CASE_INSENSITIVE);
        Matcher firstChar = pattern.matcher(email);

        if (!firstChar.find()){
            throw new IllegalStateException("Invalid email. Email cannot start with a symbol.");
        }
    }


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

        if (atSymbolIndx == -1) {
            throw new IllegalStateException("Invalid email. Missing '@'.");
        } 
        if (atSymbolCount != 1) {
            throw new IllegalStateException("Invalid email. Cannot use '@' more than once.");
        }
        if (dotSymbolIndx == -1 || dotSymbolIndx < atSymbolIndx) {
            throw new IllegalStateException("Invalid email. Placed '.' wrong or missed it.");
        }
    }
    
    
    private void checkSequenceAfterAt(String email) {
        Pattern pattern = Pattern.compile("[^abcdefghijklmnopqrstuvwxyz]");
        String afterAtStr = email.substring(atSymbolIndx + 1, dotSymbolIndx);
        Matcher afterAtMatcher = pattern.matcher(afterAtStr);

        if (afterAtMatcher.find() || afterAtStr.equals("")) {
            throw new IllegalStateException("Invalid email.");
        }
    }


    private void checkEnd(String email) {
        Pattern pattern = Pattern.compile("[^abcdefghijklmnopqrstuvwxyz]");
        String afterDotStr = email.substring(dotSymbolIndx + 1);
        Matcher afterDotMatcher = pattern.matcher(afterDotStr);

        if (afterDotMatcher.find() || afterDotStr.equals("")) {
            throw new IllegalStateException("Invalid email.");
        }
    }
}
