package com.example.myCinema.ticket;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.myCinema.movie.FSK;
import com.example.myCinema.movie.MovieVersion;
import com.example.myCinema.theatre.row.RowRank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Ticket {
    @Id
    @GeneratedValue(generator = "_ticket_id_sequnce", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "_ticket_id_sequence", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;
    
    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String movieTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieVersion movieVersion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FSK fsk;
    
    @Column(nullable = false)
    private Integer theatreNumber;
    
    @Column(nullable = false)
    private Character rowLetter;
    
    @Column(nullable = false)
    private Integer seatNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)    
    private RowRank rowRank;
    
    @Column(nullable = false)
    private Double price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Discount discount;
    
    @DateTimeFormat
    @Column(nullable = false)
    private LocalDate date;
    
    @DateTimeFormat
    @Column(nullable = false)
    private LocalTime startingTime;


    public Ticket(String userName, 
                  String movieTitle, 
                  MovieVersion movieVersion, 
                  FSK fsk, 
                  Integer theatreNumber,
                  Character rowLetter, 
                  Integer seatNumber, 
                  RowRank rowRank, 
                  Discount discount, 
                  LocalDate date,
                  LocalTime startingTime) {
        this.userName = userName;
        this.movieTitle = movieTitle;
        this.movieVersion = movieVersion;
        this.fsk = fsk;
        this.theatreNumber = theatreNumber;
        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
        this.rowRank = rowRank;
        this.discount = discount;
        this.date = date;
        this.startingTime = startingTime;
    }


    @Override
    public String toString() {
        return "Ticket for: " + this.movieTitle + "(" + this.movieVersion + ")";
    }
}