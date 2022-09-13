package com.example.myCinema.appUser;

import org.springframework.web.bind.annotation.DeleteMapping;
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


/**
 * Contains enpoint mappings for the appUser entity. For testing purposes.
 * Can only be used by a User with role 'ADMIN'.
 */
@RestController
@RequestMapping("/test/appUser")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;


//// testing


    @PostMapping("/addNew") 
    public AppUser addNew(@RequestBody AppUser appUser) {

        return appUserService.addNew(appUser);
    }


    @PutMapping("/update")
    public AppUser update(@RequestBody AppUser appUserContainer) {

        return appUserService.update(appUserContainer);
    }


    /**
     * Checking and confirming token and enabling appUser.
     * 
     * @param token of appUser to confirm.
     */
    @GetMapping("/confirmToken") 
    public void confirmToken(@RequestParam("token") String token) {

        // creating confirmationToken with token parameter 
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
    
        // confirming confirmationToken
        AppUser appUser = confirmationTokenService.confirm(confirmationToken);
    
        // saving changes made to appUser
        appUserService.save(appUser);
    }


    @GetMapping("/getByUserName")
    public AppUser getByEmail(@RequestParam("userName") String email) {

        return appUserService.getByEmail(email);
    }


    @DeleteMapping("/delete")
    public String delete(@RequestParam("userName") String email) {

        appUserService.delete(email);

        return "Deleted user with userName " + email + ".";
    }
}