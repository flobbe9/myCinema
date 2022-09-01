package com.example.myCinema.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myCinema.confirmationToken.ConfirmationToken;
import com.example.myCinema.confirmationToken.ConfirmationTokenService;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/admin/appUser")
@CrossOrigin("http://localhost:1080")
@AllArgsConstructor
public class AppUserTemplates {
    
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;


// addAppUser


    @GetMapping("/addNew")
    public String addNew(Model model) {

        // adding appUser for thymeleaf
        model.addAttribute("appUser", new AppUser());

        // initialising permissions wrapper object
        model.addAttribute("appUserWrapper", new AppUserWrapper());

        return "/admin/appUser/addNew";
    }
    
    
    @PostMapping("/addNew") 
    public String addNew(AppUser appUser, AppUserWrapper appUserWrapper, Model model) {

        // setting permissions if toggled
        if (checkToggledPermissions(appUserWrapper.getGranted())) {
            appUser.getRole().setGrantedAuthorities(setAppUserPermissions(appUserWrapper));
        }
        
        // adding appUser
        appUserService.addNew(appUser); 

        // telling thymeleaf it worked
        model.addAttribute("created", true);

        return "/admin/appUser/addNew";
    }


// confirm token


    @GetMapping("/confirmToken") 
    public String confirmToken(@RequestParam("token") String token) {

        // creating confirmationToken with token parameter 
        ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
    
        // confirming confirmationToken
        AppUser appUser = confirmationTokenService.confirm(confirmationToken);
    
        // saving changes made to appUser
        appUserService.save(appUser);

        return "/start";
    }


// deleteAppuser
    

    @GetMapping("/delete")
    public String delete(Model model) {

        // adding appUser for thymeleaf
        model.addAttribute("appUser", new AppUser());

        return "/admin/appUser/delete";
    }


    @PostMapping("/delete")
    public String delete(AppUser temp, Model model) {

        // getting email from temp appUser
        String email = temp.getEmail();
        
        // deleting appUser
        appUserService.delete(email);
        
        // telling thymeleaf it worked
        model.addAttribute("gone", true);
            
        return "/admin/appUser/delete";
    }


//// helper functions


    private Set<AppUserPermission> setAppUserPermissions(AppUserWrapper appUserWrapper) {

        // array with all permissions
        AppUserPermission[] permissionsArr = appUserWrapper.getPermissions();
        // array, telling if permission at indx is granted or not
        boolean[] granted = appUserWrapper.getGranted();        

        Set<AppUserPermission> permissions = new HashSet<AppUserPermission>();

        for (int i = 0; i < permissionsArr.length; i++) {
            // if not granted then remove permission and boolean from list
            if (granted[i]) {
                permissions.add(permissionsArr[i]);
            }
        }   

        return permissions;
    }


    private boolean checkToggledPermissions(boolean[] array) {

        for (boolean bool : array) {
            if (bool) return true;
        }

        return false;
    }
}