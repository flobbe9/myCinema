package com.example.myDiningReview.repositories;

import java.util.List;
import java.util.Optional;

import com.example.myDiningReview.models.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    List<Restaurant> findAllByZipCode(Integer zipCode);
    List<Restaurant> findAllByZipCodeAndPeanutRatingNotNull(Integer zipCode);
    List<Restaurant> findAllByZipCodeAndEggRatingNotNull(Integer zipCode);
    List<Restaurant> findAllByZipCodeAndDairyRatingNotNull(Integer zipCode);
    List<Restaurant> findAllByZipCodeAndName(Integer zipCode, String name);
}
