package com.example.myCinema.user;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.myCinema.confirmationToken.ConfirmationToken;
import com.example.myCinema.confirmationToken.ConfirmationTokenService;
import com.example.myCinema.mail.EmailValidator;
import com.example.myCinema.mail.MailService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    
    
    public AppUser addNew(AppUser appUser) {
        // checking appUser data
        if (!check(appUser) || exists(appUser.getEmail())) {
            throw new IllegalStateException("Someting wrong with appUser input data.");
        }
        
        // setting age
        setAge(appUser);

        // encoding password
        setAndEncodePassword(appUser);

        // creating confirmation token
        ConfirmationToken confirmationToken = confirmationTokenService.create(appUser);

        // sending confirmation email
        String endpoint = "http://localhost:4001/addUser/confirmToken?token=" + confirmationToken.getToken();
        String email = mailService.createEmail(Path.of("./src/main/resources/static/html/email.html"), appUser.getFirstName(), endpoint);
        mailService.send(appUser.getEmail(), email);

        return save(appUser);
    }
    
    
    public AppUser update(AppUser appUserData) {
        // checking if id is null
        if (appUserData.getId() == null) {
            throw new IllegalStateException("Id of appUserData must not be null.");
        }
        
        // getting appUser with given id from repo
        AppUser updatedAppUser = getById(appUserData.getId());

        // updating not null values from appUserData
        // firstName
        if (appUserData.getFirstName() != null) updatedAppUser.setFirstName(appUserData.getFirstName());
        // lastName
        if (appUserData.getLastName() != null) updatedAppUser.setLastName(appUserData.getLastName());
        // email/userName
        if (appUserData.getEmail() != null) updatedAppUser.setEmail(appUserData.getEmail());
        // adress
        if (appUserData.getAdress() != null) updatedAppUser.setAdress(appUserData.getAdress());
        // zipCode
        if (appUserData.getZipCode() != null) updatedAppUser.setZipCode(appUserData.getZipCode());
        // city
        if (appUserData.getCity() != null) updatedAppUser.setCity(appUserData.getCity());
        // birthday
        if (appUserData.getBirthday() != null) {
            updatedAppUser.setBirthday(appUserData.getBirthday());
        // age
            updatedAppUser.setAge(updatedAppUser.calculateAge(appUserData.getBirthday()));
        }
        
        return save(updatedAppUser);
    }


    public AppUser getById(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Could not find user with id \"" + id + "\"."));
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username).orElseThrow(() -> 
            new UsernameNotFoundException("Could not find user with username \"" + username + "\"."));
    }
    
    
    public AppUser getByUserName(String email) {
        return appUserRepository.findByEmail(email).orElseThrow(() ->
            new UsernameNotFoundException("Could not find user with username \"" + email + "\"."));
    }


    public void delete(AppUser appUser) {
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


    private boolean check(AppUser appUser) {
        // checking for null values
        if (!checkNullValues(appUser)) return false;
        
        // validate email
        EmailValidator emailValidator = new EmailValidator();
        emailValidator.validate(appUser.getEmail());

        // check birthday
        if (!checkBirthday(appUser.getBirthday())) return false;

        return true;
    }
    
    
    private boolean checkNullValues(AppUser appUser) {
        // firstName
        if (appUser.getFirstName() == null) return false;
        // lastName
        if (appUser.getLastName() == null) return false;
        // email
        if (appUser.getEmail() == null) return false;
        // password
        if (appUser.getPassword() == null) return false;
        // adress
        if (appUser.getAdress() == null) return false;
        // zipCode
        if (appUser.getZipCode() == null) return false;
        // city
        if (appUser.getCity() == null) return false;
        // birthday
        if (appUser.getBirthday() == null) return false;
        // role
        if (appUser.getRole() == null) return false;

        return true;
    }


    private boolean checkBirthday(LocalDate birthday) {
        return birthday.isBefore(LocalDate.now());
    }
    
    
    private void setAge(AppUser appUser) {
        // calculating age
        Long age = appUser.calculateAge(appUser.getBirthday());

        appUser.setAge(age);
    }


    private void setAndEncodePassword(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    }
}