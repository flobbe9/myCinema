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
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.myCinema.user.AppUser;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_confiramtionToken_id_sequence")
    @SequenceGenerator(name = "_confirmationToken_id_sequence", allocationSize = 1)
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