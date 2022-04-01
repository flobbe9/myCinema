package com.example.myDiningReview.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ConfirmationToken {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "confimationTokenSequence"
    )
    @SequenceGenerator(
        name = "confimationTokenSequence",
        allocationSize = 1
    )
    private Long id;
    private String token;
    private AppUser appUser;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    public ConfirmationToken(String token, 
                             AppUser appUser, 
                             LocalDateTime createdAt, 
                             LocalDateTime expiresAt) {
        this.token = token;
        this.appUser = appUser;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }
}