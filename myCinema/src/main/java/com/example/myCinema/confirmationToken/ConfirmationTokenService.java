package com.example.myCinema.confirmationToken;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.myCinema.user.AppUser;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    
    
    public ConfirmationToken create(AppUser appUser) {
        // creating random string as token
        String token = UUID.randomUUID().toString();
        
        // creating confirmationToken
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                                                                    appUser,
                                                                    LocalDateTime.now(),
                                                                    LocalDateTime.now().plusMinutes(15));

        return save(confirmationToken);
    }
    
    
    public AppUser confirm(ConfirmationToken confirmationToken) {
        // checking confirmationToken
        confirmationTokenValid(confirmationToken);
        
        // set confirmedAt
        confirmationToken.setConfirmedAt(LocalDateTime.now()); 
        save(confirmationToken);

        // enabling appUser
        AppUser appUser = confirmationToken.getAppUser();
        appUser.setEnabled(true);

        return appUser;
    }
    
    
    public ConfirmationToken getByToken(String token) {
        return confirmationTokenRepository.findByToken(token).orElseThrow(() -> 
            new NoSuchElementException("Could not find this confirmation token.")
        );
    }
    

    public ConfirmationToken getByAppUser(AppUser appUser) {
        return confirmationTokenRepository.findByAppUser(appUser).orElseThrow(() -> 
            new NoSuchElementException("Could not find appUser with user name \"" + appUser.getEmail() + "\"."));
    }


    public void delete(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.delete(confirmationToken);
    }


/// helper functions


    private ConfirmationToken save(ConfirmationToken confirmationToken) {
        return confirmationTokenRepository.save(confirmationToken);
    }


    private boolean confirmationTokenValid(ConfirmationToken confirmationToken) {
        if (   
            // confirmedAt
            confirmationToken.getConfirmedAt() != null ||
            // expiredAt
            confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))

                throw new IllegalStateException("Confirmation either already confirmed or expired.");


        return true;
    }
}