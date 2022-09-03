package com.example.myCinema;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class GeneralTemplates implements ErrorController {
    
    private final ExceptionService exceptionService;

    
    @GetMapping("/")
    public String getIndexPage() {

        return "index";
    }


    @GetMapping("/start") 
    public String getStartPage() {

        return "start";
    }


    @GetMapping("/login")
    public String getLoginPage() {
        
        return "login";
    }


    @GetMapping("/error")
    public String getErrorPage(HttpServletRequest request, Model model) {
        
        // object with the error
        Object obj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (obj != null) {
            // getting status code 
            int statusCode = Integer.valueOf(obj.toString());

            // getting status
            HttpStatus status = HttpStatus.valueOf(statusCode);

            return exceptionService.passExceptionToThymeleaf(status, model);
        }

        return exceptionService.passExceptionToThymeleaf(INTERNAL_SERVER_ERROR, model);
    }
}