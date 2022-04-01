package com.example.myDiningReview.controller;

import com.example.myDiningReview.models.Review;
import com.example.myDiningReview.services.AppUserService;
import com.example.myDiningReview.services.ReviewService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping("/myDiningReview")
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final AppUserService appUserService;
    
    @PostMapping("/postReview") 
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Review postReview(@RequestBody Review review) {
        String userName = appUserService.getCurrentUserName();
        review.setUserName(userName);

        return reviewService.postReview(review);
    }
    
    @ResponseStatus(code = HttpStatus.OK, reason = "Review status updated.")
    @PostMapping("/admin/checkPendingReview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Review confirmReview(@RequestBody RBody rBody) {
        Boolean status = rBody.getStatus();
        Long reviewId = rBody.getId();

        return reviewService.confirmReview(status, reviewId);
    }

    @GetMapping("/admin/getPendingList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<Review> getPendingList() {
        return reviewService.getPendingList();
    }

    @PostMapping("/admin/getReviewsByStatus")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<Review> getByStatus(@RequestBody RBody rBody) {
        Boolean status = rBody.getStatus();
        Long restaurantId = rBody.getId();

        return reviewService.getByStatus(status, restaurantId);
    }

    @AllArgsConstructor
    @Getter
    private static class RBody {
        Boolean status;
        Long id;
    }
}