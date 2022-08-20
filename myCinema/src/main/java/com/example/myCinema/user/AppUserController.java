package com.example.myCinema.user;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.myCinema.confirmationToken.ConfirmationToken;
import com.example.myCinema.confirmationToken.ConfirmationTokenService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;


    @PostMapping("/addUser") 
    public AppUser addNew(@RequestBody AppUser appUser) {
        // setting role USER
        appUser.setRole(AppUserRole.USER);

        return appUserService.addNew(appUser);
    }


    @PutMapping("/updateUser")
    public AppUser update(@RequestBody AppUser appUserData) {
        return appUserService.update(appUserData);
    }


    @GetMapping("/getUserByUserName")
    public AppUser getByUserName(@RequestParam("userName") String email) {
        return appUserService.getByUserName(email);
    }
    
    
    @GetMapping("/addUser/confirmToken") 
    public void confirmToken(@RequestParam("token") String token) {
        // creating confirmationToken with token parameter 
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);

        // confirming confirmationToken
        AppUser appUser = confirmationTokenService.confirm(confirmationToken);

        // saving changes made to appUser
        appUserService.save(appUser);
    }
}