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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionService extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
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
        if (e instanceof IllegalAccessException) return UNAUTHORIZED;
        

        // 404 not found
        if (e instanceof UsernameNotFoundException ||
            e instanceof NoSuchElementException ||
            e instanceof FileNotFoundException) 
            
                return NOT_FOUND;
        
        
        return INTERNAL_SERVER_ERROR;
    }
}