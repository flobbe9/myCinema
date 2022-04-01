package com.example.myDiningReview.services;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.myDiningReview.models.AppUser;
import com.example.myDiningReview.models.ConfirmationToken;
import com.example.myDiningReview.repositories.AppUserRepository;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    public AppUser saveUser(AppUser appUser) {
        return appUserRepository.save(appUser);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUserName(username).orElseThrow(() -> 
            new UsernameNotFoundException("Could not find user with user name " + username + "."));
    }


    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    public String register(AppUser appUser) {
        boolean exists = appUserRepository.findByUserName(appUser.getUsername()).isPresent();
        if (exists) {
            throw new IllegalStateException("User with username " + appUser.getUsername() + " does already exist.");
        }

        String password = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(password);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                                                                    appUser,
                                                                    LocalDateTime.now(),
                                                                    LocalDateTime.now().plusMinutes(15));
        confirmationTokenService.saveToken(confirmationToken);

        return token;
    }


    public AppUser enableAppUser(AppUser appUser) {
        appUser.setEnabled(true);
        return appUserRepository.save(appUser);
    }
}