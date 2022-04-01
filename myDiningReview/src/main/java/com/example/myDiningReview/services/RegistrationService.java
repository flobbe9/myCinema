package com.example.myDiningReview.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import com.example.myDiningReview.any.AppUserRole;
import com.example.myDiningReview.any.EmailValidator;
import com.example.myDiningReview.any.RegistrationRequest;
import com.example.myDiningReview.models.AppUser;
import com.example.myDiningReview.models.ConfirmationToken;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final EmailService emailService;


    public String register(RegistrationRequest registrationRequest) {
        if (registrationRequest.getUserName() == null) {
            throw new IllegalStateException("User name cannot be empty.");
        } 
        if (registrationRequest.getPassword() == null) {
            throw new IllegalStateException("Password cannot be empty.");
        } 
        if (registrationRequest.getEmail() == null) {
            throw new IllegalStateException("Email cannot be empty.");
        } else if (!emailValidator.validate(registrationRequest.getEmail())) {
            throw new IllegalStateException("Email not valid.");
        }
        if (registrationRequest.getCity() == null) {
            throw new IllegalStateException("City cannot be empty.");
        } 
        if (registrationRequest.getState() == null) {
            throw new IllegalStateException("State cannot be empty.");
        } 
        if (registrationRequest.getZipCode() == null) {
            throw new IllegalStateException("ZipCode cannot be empty.");
        } 
        if (registrationRequest.getPeanutAllergies() == null) {
            throw new IllegalStateException("Peanut allergies cannot be empty.");
        } 
        if (registrationRequest.getEggAllergies() == null) {
            throw new IllegalStateException("Egg allergies cannot be empty.");
        } 
        if (registrationRequest.getDairyAllergies() == null) {
            throw new IllegalStateException("Dairy allergies cannot be empty.");
        }

        AppUser appUser = new AppUser(registrationRequest.getUserName(),
                                      registrationRequest.getPassword(),
                                      registrationRequest.getEmail(),
                                      registrationRequest.getCity(),
                                      registrationRequest.getState(),
                                      registrationRequest.getZipCode(),
                                      registrationRequest.getPeanutAllergies(),
                                      registrationRequest.getEggAllergies(),
                                      registrationRequest.getDairyAllergies(),
                                      AppUserRole.USER
                                      );

        String token = appUserService.register(appUser);

        String confirmationLink = "http://localhost:4001/myDiningReview/register/confirmEmail?token=" + token;
        emailService.send(appUser.getEmail(), emailBuilder(appUser.getUsername(), confirmationLink));

        return token;
    }


    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
        boolean isConfirmed = confirmationToken.getConfirmedAt() != null;
        boolean expired = confirmationToken.getExpiresAt().isBefore(LocalDateTime.now());
        if (isConfirmed) {
            throw new IllegalStateException("Token already confirmed.");
        }
        if (expired) {
            throw new IllegalStateException("Token is expired.");
        }
        confirmationTokenService.updateConfirmedAt(confirmationToken);

        AppUser appUser = confirmationTokenService.getByToken(token).getAppUser();
        appUserService.enableAppUser(appUser);
    }


    public String emailBuilder(String userName, String confirmationLink) {
        Path emailPath = Path.of("./src/main/resources/static/email.html");
        try {
            String email = Files.readString(emailPath);
            return email.formatted(userName, confirmationLink);
        } catch (IOException e) {
            return e.toString();
        }
    }
}