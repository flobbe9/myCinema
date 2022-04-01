package com.example.myDiningReview.repositories;

import java.util.Optional;

import com.example.myDiningReview.models.AppUser;
import com.example.myDiningReview.models.ConfirmationToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByAppUser(AppUser appUser);
}
