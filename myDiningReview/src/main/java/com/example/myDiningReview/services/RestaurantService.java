package com.example.myDiningReview.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import com.example.myDiningReview.models.Restaurant;
import com.example.myDiningReview.repositories.RestaurantRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public Restaurant newRestaurant(Restaurant restaurant) {
        boolean empty = restaurantRepository.findAllByZipCodeAndName(restaurant.getZipCode(), 
                                                                     restaurant.getName()).isEmpty();
        if (!empty) {
            throw new IllegalStateException("Restaurant with name " + restaurant.getName() +
                " and zip code " + restaurant.getZipCode() + " does already exist.");
        }

        return restaurantRepository.save(restaurant);
    }

    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElseThrow(() -> 
            new IllegalStateException("Could not find restaurant with id " + id + "."));
    }

    public Iterable<Restaurant> getByZipCodeAndAllergie(Integer zipCode, String allergie) {
        List<Restaurant> peanutList = restaurantRepository.findAllByZipCode(zipCode);
        if (peanutList.isEmpty()) {
            throw new IllegalStateException("Could not find restaurants with zip code " + zipCode + ".");
        }

        switch(allergie) {
            case "peanut":
                return restaurantRepository.findAllByZipCodeAndPeanutRatingNotNull(zipCode);
            case "egg": 
                return restaurantRepository.findAllByZipCodeAndEggRatingNotNull(zipCode);
            case "dairy":
                return restaurantRepository.findAllByZipCodeAndDairyRatingNotNull(zipCode);
            default:
                throw new IllegalStateException("Could not find restaurants with zip code " + zipCode +
                    " and allergie rating for " + allergie + ".");
        }
    }

    public Restaurant addPeanutRating(int rating, Restaurant restaurant) {
        List<Integer> peanutRatings = restaurant.getPeanutRatings();
        peanutRatings.add(rating);

        double rounded = roundNewRating(peanutRatings);
        restaurant.setPeanutRating(rounded);

        updateTotalRating(restaurant);

        return restaurantRepository.save(restaurant);
    }

    public Restaurant addEggRating(int rating, Restaurant restaurant) {
        List<Integer> eggRatings = restaurant.getEggRatings(); 
        eggRatings.add(rating);

        double rounded = roundNewRating(eggRatings);

        restaurant.setEggRating(rounded);
        updateTotalRating(restaurant);

        return restaurantRepository.save(restaurant);
    }

    public Restaurant addDairyRating(int rating, Restaurant restaurant) {
        List<Integer> dairyRatings = restaurant.getDairyRatings(); 
        dairyRatings.add(rating);

        double rounded = roundNewRating(dairyRatings);

        restaurant.setDairyRating(rounded);
        updateTotalRating(restaurant);

        return restaurantRepository.save(restaurant);
    }

    public Restaurant updateTotalRating(Restaurant restaurant) {
        Double peanutRating = restaurant.getPeanutRating();
        Double eggRating = restaurant.getEggRating();
        Double dairyRating = restaurant.getDairyRating();

        if (peanutRating != null && eggRating != null && dairyRating != null) {
            double sum = peanutRating + eggRating + dairyRating;
            double average = sum / 3;
            BigDecimal bdl = new BigDecimal(average);
            bdl = bdl.round(new MathContext(3, RoundingMode.HALF_EVEN));

            restaurant.setTotalRating(bdl.doubleValue());

            return restaurantRepository.save(restaurant);
        } 

        return null;
    }
    
    private static Double roundNewRating(List<Integer> list) {
        double sum = list.stream().reduce(0, (a, b) -> a + b);
        double average = sum / list.size();
        BigDecimal bdl = new BigDecimal(average);
        bdl = bdl.round(new MathContext(3, RoundingMode.HALF_EVEN)); // rounding for 2 decimals

        return bdl.doubleValue();
    }
}