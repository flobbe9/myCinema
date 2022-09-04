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


/**
 * Handles some exceptions thrown by the api and recirects to errorPage for more detail.
 */
@ControllerAdvice
public class ExceptionService {

    /**
     * Passes exception message and http status to thymeleaf. 
     * Uses exception to generate correct http status. Reirects to errorPage.
     * 
     * @param e exception that was thrown.
     * @param model for passing objects to thymeleaf.
     * @return errorPage html page.
     */
    public String passExceptionToThymeleaf(Exception e, Model model) {

        // getting correct http status code for exception
        HttpStatus httpStatus = decideHttpStatus(e);

        // passing thymeleaf the httpStatus and the exception
        model.addAttribute("httpStatus", httpStatus);
        model.addAttribute("exception", e);

        return "/exception/errorPage";
    }


    /**
     * Passes exception message and http status to thymeleaf. 
     * Uses http status to generate correct exception message. Reirects to errorPage.
     * 
     * @param status httpStatus that has occurred.
     * @param model for passing objects to thymeleaf.
     * @return errorPage html page.
     */
    public String passExceptionToThymeleaf(HttpStatus status, Model model) {

        // getting appropriate error message
        String message = decideErrorMessage(status);

        // passing thymeleaf the httpStatus and the exception
        model.addAttribute("httpStatus", status);
        model.addAttribute("exception", new Exception(message));

        return "/exception/errorPage";
    }


//// helper functions


    /**
     * Assigns http status for each exception. Uses INTERNAL_SERVER_ERROR as default.
     * 
     * @param e exception that was thrown.
     * @return correct httpStatus or httpStatus.INTERNAL_SERVER_ERROR.
     */
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


    /**
     * Assigns correct error message according to the http status.
     * 
     * @param status http status.
     * @return String with error message
     */
    private String decideErrorMessage(HttpStatus status) {

        // getting status code 
        int statusCode = status.value();
        
        // exception cases
        switch (statusCode) {
            case 400:
                return "Something wrong with this url or the request.";
                
            case 401:
                return "You're not authorized to access this content.";

            case 403:
                return "You have no permission for this action.";

            case 404:
                return "This url does not exists.";

            default: 
                return "An error has occured.";
        }
    }
}