package com.example.myDiningReview.services;

import com.example.myDiningReview.models.Restaurant;
import com.example.myDiningReview.models.Review;
import com.example.myDiningReview.repositories.ReviewRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantService restaurantService;

    public Review confirmReview(Boolean status, Long id) {
        Review review = getById(id);
        review.setStatus(status);

        return reviewRepository.save(review);
    }

    public Review getById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> 
            new IllegalStateException("Could not find Review with id " + id + ".")
        );
    }

    public Review postReview(Review review) {
        Long restaurantId = review.getRestaurantId();
        if (restaurantId == null) {
            throw new IllegalStateException("Restaurant id cannot be empty.");
        }

        Restaurant restaurant = restaurantService.getById(restaurantId);
        if (review.getPeanutRating() != null) {
            restaurantService.addPeanutRating(review.getPeanutRating(), restaurant);
        }
        if (review.getEggRating() != null) {
            restaurantService.addEggRating(review.getEggRating(), restaurant);
        }
        if (review.getDairyRating() != null) {
            restaurantService.addDairyRating(review.getDairyRating(), restaurant);
        }

        return reviewRepository.save(review);
    }

    public Iterable<Review> getPendingList() {
        return reviewRepository.findAllByStatus(null);
    }

    public Iterable<Review> getByStatus(Boolean status, Long restaurantId) {
        return reviewRepository.findAllByStatusAndRestaurantId(status, restaurantId);
    }
}
