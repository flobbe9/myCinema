package com.example.myCinema.confirmationToken;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.myCinema.user.AppUser;

import lombok.AllArgsConstructor;


 /** 
  * Contains methods to use endpoints and persist confirmationTokens.
  */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    
    
    /**
     * Create confirmationToken with random token, expiresAt time, createdAt time and the corresponding appUser.
     * Saves it to db.
     * 
     * @param appUser who the token is for.
     * @return saved confirmationToken
     */
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
    
    
    /**
     * Confirms token if it is valid and sets confirmedAt time.
     * Enables appUsers account.
     * Saves changes only for confirmationToken.
     * 
     * @param confirmationToken to confirm.
     * @return appUser whos account was enabled.
     */
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


    /**
     * Checks confirmationToken. Throws Exception if one check is unsuccessful.
     * 
     * <p>Should not be confirmed already.
     * <p>Should not be expired already.
     * 
     * 
     * @param confirmationToken to validate.
     * @return true if all checks were successfull.
     */
    private boolean confirmationTokenValid(ConfirmationToken confirmationToken) {

        if (// confirmedAt
            confirmationToken.getConfirmedAt() != null ||
            // expiredAt
            confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()))

                throw new IllegalStateException("Confirmation token either already confirmed or expired.");

        return true;
    }
}