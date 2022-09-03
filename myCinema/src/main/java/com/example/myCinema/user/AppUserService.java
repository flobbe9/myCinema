package com.example.myCinema.user;

import java.nio.file.Path;
import java.time.LocalDate;
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

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AppUserService extends CheckEntity implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    
    
    public AppUser addNew(AppUser appUser) {

        // checking appUser
        appUserValid(appUser);
        
        // checking if appUser does already exist
        if (exists(appUser.getEmail())) 
            throw new IllegalStateException("App user with username \"" + appUser.getEmail() + "\" does already exist.");
        
        // setting age
        setAge(appUser);

        // encoding password
        setAndEncodePassword(appUser, appUser.getPassword());

        // creating confirmation token
        ConfirmationToken confirmationToken = confirmationTokenService.create(appUser);

        // sending confirmation email
        String token = confirmationToken.getToken();
        String email = mailService.createEmail(Path.of("./src/main/resources/templates/admin/appUser/email.html"), appUser.getFirstName(), token);
        mailService.send(appUser.getEmail(), email);

        return save(appUser);
    }
    
    
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


    private boolean appUserValid(AppUser appUser) {

        // validate email
        EmailValidator emailValidator = new EmailValidator();
        emailValidator.validate(appUser.getEmail());

        // checking for null values
        hasNullValue(appUser);

        // check birthday
        if (!checkBirthday(appUser.getBirthday())) 
            throw new IllegalStateException("Birthday cannot be in the future.");

        return true;
    }
    
    
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
}