package com.example.myDiningReview.repositories;

import java.util.List;

import com.example.myDiningReview.models.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByStatus(Boolean status);
    List<Review> findAllByStatusAndRestaurantId(Boolean status, Long restaurantId);
}
