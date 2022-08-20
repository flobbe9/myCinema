package com.example.myCinema.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
public class ExceptionService {
    public String passExceptionToThymeleaf(Exception e, Model model) {
        // getting correct http status code for exception
        HttpStatus httpStatus = decideHttpStatus(e);

        // passing thymeleaf the httpStatus and the exception
        model.addAttribute("httpStatus", httpStatus);
        model.addAttribute("exception", e);

        return "/exception/errorPage";
    }


//// helper functions


    private HttpStatus decideHttpStatus(Exception e) {
        // 401 unauthorized
        if (e.getClass().equals(IllegalAccessException.class)) {

            return UNAUTHORIZED;
        }

        // 404 not found
        if (e.getClass().equals(UsernameNotFoundException.class) ||
            e.getClass().equals(NoSuchElementException.class) ||
            e.getClass().equals(FileNotFoundException.class)) {
                
            return NOT_FOUND;
        }
        
        return INTERNAL_SERVER_ERROR;
    }
}