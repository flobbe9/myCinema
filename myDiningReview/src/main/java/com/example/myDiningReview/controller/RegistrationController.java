package com.example.myDiningReview.controller;

import com.example.myDiningReview.any.RegistrationRequest;
import com.example.myDiningReview.services.RegistrationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/myDiningReview/register")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;


    // @ResponseStatus(code = HttpStatus.OK, reason = "Successfully registered.")
    @PostMapping
    public String registerAppUser(@RequestBody RegistrationRequest registrationRequest) {
        return registrationService.register(registrationRequest);
    }


    @GetMapping("/confirmEmail") 
    public String confirmEmail(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return "confirmed";
    }
}