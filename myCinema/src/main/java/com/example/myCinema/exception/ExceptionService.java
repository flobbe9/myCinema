package com.example.myCinema.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ExceptionService {

    @ExceptionHandler(Throwable.class)
    public String passExceptionToThymeleaf(Exception e, Model model) {
        // getting correct http status code for exception
        HttpStatus httpStatus = decideHttpStatus(e);

        // passing thymeleaf the httpStatus and the exception
        model.addAttribute("httpStatus", httpStatus);
        model.addAttribute("exception", e);

        return "/exception/errorPage";
    }


    public String passExceptionToThymeleaf(HttpStatus status, Model model) {
        // getting appropriate error message
        String message = decideErrorMessage(status, model);

        // passing thymeleaf the httpStatus and the exception
        model.addAttribute("httpStatus", status);
        model.addAttribute("exception", new Exception(message));

        return "/exception/errorPage";
    }


//// helper functions


    private HttpStatus decideHttpStatus(Exception e) {
        // 401 unauthorized
        if (e instanceof IllegalAccessException) return UNAUTHORIZED;
        

        // 404 not found
        if (e instanceof UsernameNotFoundException ||
            e instanceof NoSuchElementException ||
            e instanceof FileNotFoundException) 
            
                return NOT_FOUND;
        
        
        return INTERNAL_SERVER_ERROR;
    }


    private String decideErrorMessage(HttpStatus status, Model model) {
        // getting status code 
        int statusCode = status.value();
        
        // exception cases
        switch (statusCode) {
            case 400:
                return "Bad Request";
                
            case 401:
                return "Not authorized.";

            case 403:
                return "You have no permission for this action.";

            case 404:
                return "This page does not exists.";

            default: 
                return "An error has occured. Status code not available.";
        }
    }
}