package com.example.myCinema.appUser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;


/**
 * Controller for users. Contains endpoints for actions with user permission. Methods work with thymeleaf templates.
 * Can be accessed by a user with role 'USER' or 'ADMIN'. 
 */
@Controller
@RequestMapping("/appUser")
@AllArgsConstructor
public class AppUserControllerUser {
    
    private final AppUserService appUserService;


// register


    @GetMapping("/register")
    public String register(Model model) {

        // passing empty appUser object to thymeleaf
        model.addAttribute("appUser", new AppUser());

        return "user/appUser/register";
    }


    @PostMapping("/register")
    public String register(AppUser appUser, Model model) {

        try {
            // setting role
            appUser.setRole(AppUserRole.USER);
            
            // adding appUser
            appUserService.addNew(appUser); 

            // telling thymeleaf it worked
            model.addAttribute("created", true);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "user/appUser/register";
    }
}