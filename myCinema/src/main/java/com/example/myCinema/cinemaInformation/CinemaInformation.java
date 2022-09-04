package com.example.myCinema.cinemaInformation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Represents a cinema. Contains general information you would find on the
 * 'about' page of the website.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class CinemaInformation {
    
    @Id
    @GeneratedValue(generator = "_cinema_information_id_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "_cinema_information_id_sequence", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private Integer zipCode;
    
    @Column(nullable = false)
    private String adress;
    
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;


    public CinemaInformation(String name, 
                             String city, 
                             Integer zipCode, 
                             String adress, 
                             String email, 
                             String phoneNumber) {

        this.name = name;
        this.city = city;
        this.zipCode = zipCode;
        this.adress = adress;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }


    @Override
    public String toString() {
        
        return this.name + " " + this.city;
    }
}