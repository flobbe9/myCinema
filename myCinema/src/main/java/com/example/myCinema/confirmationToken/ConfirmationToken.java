package com.example.myCinema.confirmationToken;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.myCinema.appUser.AppUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * For user account validation. Contains random string token to identify. 
 * Is confirmed by user via email.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appUser_id")
    private AppUser appUser;

    @DateTimeFormat
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @DateTimeFormat
    private LocalDateTime confirmedAt;
    
    @DateTimeFormat
    @Column(nullable = false)
    private LocalDateTime expiresAt;


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