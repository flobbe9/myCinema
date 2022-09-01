package com.example.myCinema.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/test/appUser")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;


//// testing


    @PostMapping("/addNew") 
    public AppUser addNew(@RequestBody AppUser appUser) {

        return appUserService.addNew(appUser);
    }


    @PutMapping("/update")
    public AppUser update(@RequestBody AppUser appUserContainer) {

        return appUserService.update(appUserContainer);
    }


    @GetMapping("/getByUserName")
    public AppUser getByEmail(@RequestParam("userName") String email) {

        return appUserService.getByEmail(email);
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam("userName") String email) {

        appUserService.delete(email);
    }
}