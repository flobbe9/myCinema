package com.example.myDiningReview.controller;

import com.example.myDiningReview.models.Restaurant;
import com.example.myDiningReview.services.RestaurantService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping("/myDiningReview")
@AllArgsConstructor
public class RestaurantController {
    private final RestaurantService restauranteService;
    
    @PostMapping("/newRestaurant")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Restaurant newRestaurant(@RequestBody Restaurant restaurant) {
        return restauranteService.newRestaurant(restaurant);
    }

    @PostMapping("/getRestaurant")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Restaurant getRestaurant(@RequestBody Long id) {
        return restauranteService.getById(id);
    }
    
    @PostMapping("/getRestaurantsByZipCodeAndAllergie")
    public Iterable<Restaurant> getByZipCodeAndAllergie(@RequestBody RBody rBody) {
        Integer zipCode = rBody.getNum();
        // use singular for allergies like: "peanut" for peanutAllergies or "dairy" for dairyAllergies
        String allergie = rBody.getStr();

        return restauranteService.getByZipCodeAndAllergie(zipCode, allergie);
    }

    // Object to use as requestBody
    @AllArgsConstructor
    @Getter
    private static class RBody {
        private Integer num;
        private String str;
    }
}

