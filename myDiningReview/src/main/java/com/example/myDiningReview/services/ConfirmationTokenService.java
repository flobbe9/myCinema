package com.example.myDiningReview.services;

import java.time.LocalDateTime;

import com.example.myDiningReview.models.AppUser;
import com.example.myDiningReview.models.ConfirmationToken;
import com.example.myDiningReview.repositories.ConfirmationTokenRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;


    public ConfirmationToken saveToken(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }

    
    public ConfirmationToken getByUser(AppUser appUser) {
        return confirmationTokenRepository.findByAppUser(appUser).orElseThrow(() -> 
            new IllegalStateException("Could not find token from app user " + appUser.getUsername() + "."));
    }


    public ConfirmationToken getByToken(String token) {
        return confirmationTokenRepository.findByToken(token).orElseThrow(() -> 
            new IllegalStateException("Could not find token."));
    }
    

    public ConfirmationToken updateConfirmedAt(ConfirmationToken confirmationToken) {
        confirmationToken.setConfirmedAt(LocalDateTime.now());

        return confirmationTokenRepository.save(confirmationToken);
    }
}
