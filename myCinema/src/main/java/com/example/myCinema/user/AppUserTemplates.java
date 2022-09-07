package com.example.myCinema.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
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


/**
 * Controller for admins. Contains endpoints to alter appUsers, delete or add one. Methods work with thymeleaf templates.
 * Can only be accessed by user with role 'ADMIN'. 
 * 
 * <p>{@code @CrossOrigin} allows the mail server to access this controller.
 */
@Controller
@RequestMapping("/admin/appUser")
@CrossOrigin("http://localhost:1080")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
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

        return "admin/appUser/addNew";
    }
    
    
    /**
     * Tries to add new appUser. Sets permissions if some were selected and passes confirm boolean to 
     * thymeleaf or, if an exception is thrown, the errorMessage.
     * 
     * @param appUser to add.
     * @param movieWrapper for helping the html page.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
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

        return "admin/appUser/addNew";
    }


// confirm token


    /**
     * Directs user to login page if successful or to errorPage if not. Confirms token that is 
     * passed through pathvariable and enables appUser.
     * 
     * @param token to confirm.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
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

        return "login";
    }


// deleteAppuser
    

    @GetMapping("/delete")
    public String delete(Model model) {

        // adding appUser for thymeleaf
        model.addAttribute("appUser", new AppUser());

        return "admin/appUser/delete";
    }


    /**
     * Tries to delete appUser. Stays at delete page if successful and displays errorMessage on same page
     * if not.
     * 
     * @param appUser to delete.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/delete")
    public String delete(AppUser appUser, Model model) {

        try {
            // getting email from temp appUser
            String email = appUser.getEmail();
            
            // deleting appUser
            appUserService.delete(email);
            
            // telling thymeleaf it worked
            model.addAttribute("gone", true);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }
            
        return "admin/appUser/delete";
    }


//// helper functions


    /**
     * Getting array with booleans for each selected permission and adding permissions with true 
     * value to set.
     * 
     * @param appUserWrapper contains granted permissions array.
     * @return set with AppUserPermissions.
     */
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


    /**
     * Checking if any permissions have been selected on html page.
     * 
     * @param granted array with booleans representing each permission.
     * @return true if array contains at least one true value, false otherwise.
     */
    private boolean checkToggledPermissions(boolean[] granted) {

        for (boolean bool : granted) {
            if (bool) return true;
        }

        return false;
    }
}