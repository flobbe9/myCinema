package com.example.myCinema.theatre;

import static com.example.myCinema.theatre.row.RowRank.BOX;
import static com.example.myCinema.theatre.row.RowRank.PARQUET;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.example.myCinema.theatre.row.Row;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_theatre_id_sequence")
    @SequenceGenerator(name = "_theatre_id_sequence", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(nullable = false)
    private Boolean threeD;
    
    @Column(nullable = false)
    private Integer rowsTotal; 

    @Column(nullable = false)
    private Integer seatsTotal;

    @Column(nullable = false)
    private Integer seatsPerRow;
    
    @Column(nullable = false)
    private Boolean hasLoveSeats; 
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "theatre_id")
    @JsonManagedReference
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private List<Row> rows;
    
    public static final Integer MAX_ROWS = 26;

    public static final Integer NUM_ROWS_FOR_BIG_CINEMA = 15;   

    public static final Integer NUM_SEATS_PER_ROW_NORMAL_CINEMA = 15;
    
    public static final Integer NUM_SEATS_PER_ROW_BIG_CINEMA = 20;
    
    public static final Double BASIC_PRICE = 10.0;


    public Theatre(Integer number, 
                   boolean threeD, 
                   Integer rowsTotal) {
        this.number = number;
        this.threeD = threeD;
        this.rowsTotal = rowsTotal;
    }


    public void setFieldVariables() {
        this.seatsPerRow = calculateSeatsPerRow();
        this.seatsTotal = rowsTotal * seatsPerRow;
        this.hasLoveSeats = rowsTotal >= NUM_ROWS_FOR_BIG_CINEMA;
        this.rows = generateAllRows(rowsTotal);
    }


    @Override 
    public String toString() {
        return "Theatre " + getNumber();
    }


/// helper functions


    private List<Row> generateAllRows(int rowsTotal) {
        List <Row> rows = new LinkedList<Row>();

        for (int i = 0; i < rowsTotal; i++) {
            boolean frontRow = i == rowsTotal - 1;

            // small theatre
            if (rowsTotal < NUM_ROWS_FOR_BIG_CINEMA) { 
                if (i < rowsTotal / 2) { 
                    // box back
                    rows.add(new Row((char) (65 + i), BOX, seatsPerRow, frontRow));
                } else {   
                    // parquet front          
                    rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
                }

            // big theatre
            } else {              
                if (i < rowsTotal / 3) {
                    // parquet back
                    rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
                } else if (i < (int) (rowsTotal * (2.0 / 3))) {
                    // box middle
                    rows.add(new Row((char) (65 + i), BOX, seatsPerRow, frontRow));
                } else {
                    // parquet front
                    rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
                }
            }
        }
        
        return rows;
    }


    private int calculateSeatsPerRow() {
        return (rowsTotal < NUM_ROWS_FOR_BIG_CINEMA) ? NUM_SEATS_PER_ROW_NORMAL_CINEMA : NUM_SEATS_PER_ROW_BIG_CINEMA;
    }
}