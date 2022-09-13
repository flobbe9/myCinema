package com.example.myCinema.confirmationToken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myCinema.appUser.AppUser;


@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    
    Optional<ConfirmationToken> findByToken(String token);
    
    Optional<ConfirmationToken> findByAppUser(AppUser appUser);
}