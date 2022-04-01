package com.example.myDiningReview.models;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.ElementCollection;
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
public class Restaurant {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "restaurantSequence"
    )
    @SequenceGenerator(
        name = "restaurantSequence",
        allocationSize = 1
    )
    private Long id;
    private String name;
    private Integer zipCode;
    private Double totalRating;
    @ElementCollection
    private List<Integer> peanutRatings;
    private Double peanutRating;
    @ElementCollection
    private List<Integer> eggRatings;
    private Double eggRating;
    @ElementCollection
    private List<Integer> dairyRatings;
    private Double dairyRating;
    
    public Restaurant(String name, Integer zipCode) {
        this.name = name;
        this.zipCode = zipCode;
    }

    public void addPeanutRating(int rating) {
        peanutRatings.add(rating);

        double sum = peanutRatings.stream().reduce(0, (a, b) -> a + b);
        double average = sum / peanutRatings.size();
        BigDecimal bdl = new BigDecimal(average);
        bdl = bdl.round(new MathContext(2, RoundingMode.HALF_EVEN)); // rounding for 1 decimal

        setPeanutRating(bdl.doubleValue());
    }

    public void addEggRating(int rating) {
        peanutRatings.add(rating);

        double sum = eggRatings.stream().reduce(0, (a, b) -> a + b);
        double average = sum / eggRatings.size();
        BigDecimal bdl = new BigDecimal(average);
        bdl = bdl.round(new MathContext(2, RoundingMode.HALF_EVEN)); // rounding for 1 decimal

        setEggRating(bdl.doubleValue());
    }

    public void addDairyRating(int rating) {
        peanutRatings.add(rating);

        double sum = dairyRatings.stream().reduce(0, (a, b) -> a + b);
        double average = sum / dairyRatings.size();
        BigDecimal bdl = new BigDecimal(average);
        bdl = bdl.round(new MathContext(2, RoundingMode.HALF_EVEN)); // rounding for 1 decimal

        setDairyRating(bdl.doubleValue());
    }

    public void updateTotalRating() {
        if (getPeanutRating() != null && getEggRating() != null && getDairyRating() != null) {
            double sum = getPeanutRating() + getEggRating() + getDairyRating();
            double average = sum / 3;
            BigDecimal bdl = new BigDecimal(average);
            bdl = bdl.round(new MathContext(2, RoundingMode.HALF_EVEN));

            setTotalRating(bdl.doubleValue());
        } 
    }
}
