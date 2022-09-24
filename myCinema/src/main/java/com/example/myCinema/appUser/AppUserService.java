package com.example.myCinema.appUser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myCinema.CheckEntity;
import com.example.myCinema.confirmationToken.ConfirmationToken;
import com.example.myCinema.confirmationToken.ConfirmationTokenService;
import com.example.myCinema.mail.EmailValidator;
import com.example.myCinema.mail.MailService;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;


/**
 * Contains methods to use endpoints and persist the appUser entity.
 * 
 * <p>Extends CheckEntity for checking objects and collections.
 * <p>Implements UserDetailsService.
 */
@Service
@AllArgsConstructor
public class AppUserService extends CheckEntity implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    
    
    /**
     * Tries to add new appUser, checks fields and creates confirmationToken for appUser.
     * Sends confirmation email including the token.
     * 
     * <p> All paths are relative to the 'mail' folder, not to the 'appUser' folder.
     * 
     * @param appUser to add.
     * @return saved appUser.
     * @throws IOException
     */
    public AppUser addNew(AppUser appUser) {

        // checking appUser
        appUserValid(appUser);

        // checking for null values
        hasNullValue(appUser);
        
        // checking if appUser does already exist
        if (exists(appUser.getEmail())) 
            throw new IllegalStateException("User with username \"" + appUser.getEmail() + "\" does already exist.");
        
        // setting age
        setAge(appUser);

        // encoding password
        setAndEncodePassword(appUser, appUser.getPassword());

        // creating confirmation token
        ConfirmationToken confirmationToken = confirmationTokenService.create(appUser);

        // sending confirmation email
        String path = "./html/accountConfirmationEmail.html";
        mailService.send(path,
                         getAccountConfirmationEmailFillInList(confirmationToken), 
                         appUser.getEmail(), 
                        null);

        return save(appUser);
    }
    
    
    /**
     * Tries to update appUser, using not null fields of appUserContainer. Throws exception if check unsuccessfull or
     * id of appUserContainer is null.
     * 
     * @param appUserContainer with new values and id.
     * @return updated appUser.
     */
    public AppUser update(AppUser appUserContainer) {

        // checking if id is null
        if (appUserContainer.getId() == null) 
            throw new IllegalStateException("Id of appUserContainer must not be null.");
        
        // getting appUser with given id from repo
        AppUser appUserToUpdate = getById(appUserContainer.getId());

        // firstName
        if (!objectNullOrEmpty(appUserContainer.getFirstName())) appUserToUpdate.setFirstName(appUserContainer.getFirstName());
        // lastName
        if (!objectNullOrEmpty(appUserContainer.getLastName())) appUserToUpdate.setLastName(appUserContainer.getLastName());
        // email
        if (!objectNullOrEmpty(appUserContainer.getEmail())) appUserToUpdate.setEmail(appUserContainer.getEmail());
        // password
        if (!objectNullOrEmpty(appUserContainer.getPassword())) setAndEncodePassword(appUserToUpdate, appUserContainer.getPassword());
        // adress
        if (!objectNullOrEmpty(appUserContainer.getAdress())) appUserToUpdate.setAdress(appUserContainer.getAdress());
        // zipCode
        if (!objectNullOrEmpty(appUserContainer.getZipCode())) appUserToUpdate.setZipCode(appUserContainer.getZipCode());
        // city
        if (!objectNullOrEmpty(appUserContainer.getCity())) appUserToUpdate.setCity(appUserContainer.getCity());

        if (!objectNullOrEmpty(appUserContainer.getBirthday())) {
            // birthday
            appUserToUpdate.setBirthday(appUserContainer.getBirthday());
            // age
            appUserToUpdate.setAge(appUserToUpdate.calculateAge(appUserContainer.getBirthday()));
        }

        // checking updatedAppUser
        appUserValid(appUserToUpdate);
        
        return save(appUserToUpdate);
    }


    public AppUser getById(Long id) {

        return appUserRepository.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Could not find user with id \"" + id + "\"."));
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appUserRepository.findByEmail(email).orElseThrow(() -> 
            new UsernameNotFoundException("Could not find user with username \"" + email + "\"."));
    }
    
    
    public AppUser getByEmail(String email) {

        return appUserRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("Could not find user with username \"" + email + "\"."));
    }


    public void delete(String email) {

        // getting appUser by email
        AppUser appUser = getByEmail(email);

        // deleting confirmationToken
        ConfirmationToken confirmationToken = confirmationTokenService.getByAppUser(appUser);
        confirmationTokenService.delete(confirmationToken);

        appUserRepository.delete(appUser);
    }
    

    public AppUser save(AppUser appUser) {

        return appUserRepository.save(appUser);
    }


    public boolean exists(String email) {

        // checking by email 
        return appUserRepository.findByEmail(email).isPresent();
    }


//// helper functions:


    /**
     * Checking appUsers fields. Throws and exception if check unsuccessful.
     * 
     * @param appUser to check.
     * @return true if nothing wrong with fields.
     */
    private boolean appUserValid(AppUser appUser) {

        // validate email
        EmailValidator emailValidator = new EmailValidator();
        emailValidator.validate(appUser.getEmail());

        // check birthday
        if (!checkBirthday(appUser.getBirthday())) 
            throw new IllegalStateException("Birthday cannot be in the future.");

        return true;
    }
    
    
    /**
     * Checking for null values and empty strings. Throws exception if one is found.
     * 
     * @param appUser to check.
     * @return false if no null value or empty string is present.
     */
    private boolean hasNullValue(AppUser appUser) {

        if (// firstName
            appUser.getFirstName() == null ||
            // lastName
            appUser.getLastName() == null ||
            // email
            appUser.getEmail() == null ||
            // password
            appUser.getPassword() == null ||
            // adress
            appUser.getAdress() == null ||
            // zipCode
            appUser.getZipCode() == null ||
            // city
            appUser.getCity() == null ||
            // birthday
            appUser.getBirthday() == null ||
            // role
            appUser.getRole() == null)

                throw new IllegalStateException("AppUser data contains null value or empty strings ('').");

        return false;
    }


    private boolean checkBirthday(LocalDate birthday) {

        return birthday.isBefore(LocalDate.now());
    }
    
    
    private void setAge(AppUser appUser) {

        // calculating age
        Long age = appUser.calculateAge(appUser.getBirthday());

        appUser.setAge(age);
    }


    private void setAndEncodePassword(AppUser appUser, String password) {

        appUser.setPassword(passwordEncoder.encode(password));
    }


    /**
     * Creating list with string variables the account confirmation email should be formatted with.
     * 
     * @param confirmationToken contains data for the email.
     * @return list with string variables for the email.
     */
    private List<String> getAccountConfirmationEmailFillInList(ConfirmationToken confirmationToken) {
        
        // getting first name
        String firstName = confirmationToken.getAppUser().getFirstName();

        // getting token
        String token = confirmationToken.getToken();

        return Lists.newArrayList(firstName, token);
    }
}