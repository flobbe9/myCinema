package com.example.myCinema.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.confirmationToken.ConfirmationToken;
import com.example.myCinema.confirmationToken.ConfirmationTokenService;
import com.example.myCinema.exception.ExceptionService;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/admin/appUser")
@CrossOrigin("http://localhost:1080")
@AllArgsConstructor
public class AppUserTemplates {
    
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final ExceptionService exceptionService;


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

        try {
            // setting permissions if toggled
            if (checkToggledPermissions(appUserWrapper.getGranted())) {
                appUser.getRole().setGrantedAuthorities(setAppUserPermissions(appUserWrapper));
            }
            
            // adding appUser
            appUserService.addNew(appUser); 

            // telling thymeleaf it worked
            model.addAttribute("created", true);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "/admin/appUser/addNew";
    }


// confirm token


    @GetMapping("/confirmToken/{token}") 
    public String confirmToken(@PathVariable("token") String token, Model model) {

        try {
            // creating confirmationToken with token parameter 
            ConfirmationToken confirmationToken = confirmationTokenService.getByToken(token);
        
            // confirming confirmationToken
            AppUser appUser = confirmationTokenService.confirm(confirmationToken);
        
            // saving changes made to appUser
            appUserService.save(appUser);

        } catch (Exception e) {
            // passing error message to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

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

        try {
            // getting email from temp appUser
            String email = temp.getEmail();
            
            // deleting appUser
            appUserService.delete(email);
            
            // telling thymeleaf it worked
            model.addAttribute("gone", true);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }
            
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