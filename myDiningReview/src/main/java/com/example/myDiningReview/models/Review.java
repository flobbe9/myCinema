package com.example.myDiningReview.models;

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
public class Review {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "reviewSequence"
    )
    @SequenceGenerator(
        name = "reviewSequence",
        allocationSize = 1
    )
    private Long id;
    private String userName;
    private Long restaurantId;
    private Boolean status; // true meaning accepted, false meaning not accepted, null meaning no reviewed yet
    private Integer peanutRating;
    private Integer eggRating;
    private Integer dairyRating;
    private String commentary;

    public Review(Long restaurantId, 
                  String userName,
                  Integer peanutRating, 
                  Integer eggRating, 
                  Integer dairyRating, 
                  String commentary) {
        this.restaurantId = restaurantId;
        this.userName = userName;
        this.peanutRating = peanutRating;
        this.eggRating = eggRating;
        this.dairyRating = dairyRating;
        this.commentary = commentary;
    }
}