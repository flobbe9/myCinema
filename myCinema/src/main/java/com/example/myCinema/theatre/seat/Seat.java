package com.example.myCinema.theatre.seat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.example.myCinema.theatre.row.Row;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "theatre_seats")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Seat {

    @Id
    @GeneratedValue(generator = "_seat_id_sequence", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "_seat_id_sequence", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private Character rowLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "row_id")
    @JsonBackReference
    private Row row;

    @Column(nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;

    @Column(nullable = false)
    private Boolean taken = false;
    

    public Seat(Character rowLetter, 
                Integer seatNumber, 
                SeatType seatType) {

        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
    }


    @Override
    public String toString() {
        
        return "Seat " + this.seatNumber;
    }
}