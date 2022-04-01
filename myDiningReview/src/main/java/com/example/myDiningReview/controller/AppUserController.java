package com.example.myDiningReview.controller;

import com.example.myDiningReview.models.AppUser;
import com.example.myDiningReview.services.AppUserService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/myDiningReview")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/getUser")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    public UserDetails getAppUser() {
        String userName = appUserService.getCurrentUserName();
        return appUserService.loadUserByUsername(userName);
    }

    @PutMapping("/update") // username can't be updated
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN')")
    public AppUser updateAppUser(@RequestBody AppUser updatedAppUser) {
        String userName = appUserService.getCurrentUserName();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(userName);
        if (updatedAppUser.getUsername() != null) {
            throw new IllegalStateException("User name cannot be changed.");
        }
        
        if (updatedAppUser.getPassword() != null) {
            appUser.setPassword(updatedAppUser.getPassword());
        }
        if (updatedAppUser.getEmail() != null) {
            appUser.setEmail(updatedAppUser.getEmail());
        }
        if (updatedAppUser.getCity() != null) {
            appUser.setCity(updatedAppUser.getCity());
        }
        if (updatedAppUser.getState() != null) {
            appUser.setState(updatedAppUser.getState());
        }
        if (updatedAppUser.getZipCode() != null) {
            appUser.setZipCode(updatedAppUser.getZipCode());
        }
        if (updatedAppUser.getPeanutAllergies() != null) {
            appUser.setPeanutAllergies(updatedAppUser.getPeanutAllergies());
        }
        if (updatedAppUser.getEggAllergies() != null) {
            appUser.setEggAllergies(updatedAppUser.getEggAllergies());
        }
        if (updatedAppUser.getDairyAllergies() != null) {
            appUser.setDairyAllergies(updatedAppUser.getDairyAllergies());
        }

        return appUserService.saveUser(appUser);
    }
}