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

import com.example.myCinema.theatre.row.Row;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Contains theatre information and some static variables that help deciding how to size theatres.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer number;

    @Column(nullable = false)
    private Boolean threeD;
    
    @Column(nullable = false)
    private Integer rowsTotal; 

    private Integer seatsTotal;

    private Integer seatsPerRow;
    
    private Boolean hasLoveSeats; 
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "theatre_id")
    @JsonManagedReference
    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private List<Row> rows;
    
    /** Maximum number of rows in a theatre. */
    public static final Integer MAX_ROWS = 26;

    /** Minimum number of rows in a theatre so it is considered as 'big'. */
    public static final Integer NUM_ROWS_BIG_THEATRE = 15;   

    /** Number of seats per row in a 'normal' sized cinema. 
     * @see #NUM_ROWS_BIG_THEATRE
    */
    public static final Integer NUM_SEATS_PER_ROW_NORMAL_THEATRE = 15;
    
    /** Number of seats per row in 'big' sized cinema.
     * @see #NUM_ROWS_BIG_THEATRE
     */
    public static final Integer NUM_SEATS_PER_ROW_BIG_THEATRE = 20;
    
    /** Basic price for each ticket. */
    public static final Double BASIC_PRICE = 10.0;


    public Theatre(Integer number, 
                   boolean threeD, 
                   Integer rowsTotal) {

        this.number = number;
        this.threeD = threeD;
        this.rowsTotal = rowsTotal;

        // setting other fields
        setFieldVariables();
    }


    /** Setting row list and some fields according to information from constructor. */
    public void setFieldVariables() {

        this.seatsPerRow = calculateSeatsPerRow();
        this.seatsTotal = rowsTotal * seatsPerRow;
        this.hasLoveSeats = rowsTotal >= NUM_ROWS_BIG_THEATRE;
        this.rows = generateAllRows(rowsTotal);
    }


    @Override 
    public String toString() {

        return "Theatre " + getNumber();
    }


/// helper functions


    /**
     * Generates all rows for theatre according to the size.
     * 
     * @see #NUM_ROWS_BIG_THEATRE
     * @param rowsTotal total number of rows in the cinema.
     * @return list with all row objects.
     */
    private List<Row> generateAllRows(int rowsTotal) {

        List <Row> rows = new LinkedList<Row>();

        for (int i = 0; i < rowsTotal; i++) {
            // row with highest index is front row
            boolean frontRow = (i == rowsTotal - 1);

            // small theatre
            if (rowsTotal < NUM_ROWS_BIG_THEATRE) { 
                generateRowsSmallTheatre(i, frontRow, rows);

            // big theatre
            } else {    
                generateRowsBigTheatre(i, frontRow, rows);          
            }
        }
        
        return rows;
    }


    /**
     * A small theatre has one box section in the back (lower characters) and one parquet section 
     * in the front (higher characters). 
     * 
     * <p>Each section gets halve of the seats (rounding down so in case 
     * of an odd number of rows there's one parquet row more).
     * 
     * @param i index of the for loop in {@link #generateAllRows(int)}.
     * @param frontRow true if row is the front row.
     * @param rows list with row objects.
     */
    private void generateRowsSmallTheatre(int i, boolean frontRow, List<Row> rows) {
        
        // lower row numbers
        if (i < rowsTotal / 2) { 
            // box rows in the back
            rows.add(new Row((char) (65 + i), BOX, seatsPerRow, frontRow));

        // higher row numbers are parquet rows
        } else {   
            // parquet rows in the front      
            rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
        }
    }


    /**
     * A big theatre has two sections with parquet rows, one in the back and one in the front. The box section
     * is in between those two.
     * 
     * <p>Each section gets a third of the total rows.
     * 
     * @param i index of the for loop in {@link #generateAllRows(int)}.
     * @param frontRow true if row is the front row.
     * @param rows list with row objects.
     */
    private void generateRowsBigTheatre(int i, boolean frontRow, List<Row> rows) {

        // highest row numbers
        if (i < rowsTotal / 3) {
            // parquet rows in the back
            rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
        
        // middle row numbers
        } else if (i < (int) (rowsTotal * (2.0 / 3))) {
            // box rows in the middle
            rows.add(new Row((char) (65 + i), BOX, seatsPerRow, frontRow));

        // lowest row numbers
        } else {
            // parquet rows in the front
            rows.add(new Row((char) (65 + i), PARQUET, seatsPerRow, frontRow));
        }
    }


    private int calculateSeatsPerRow() {

        return (rowsTotal < NUM_ROWS_BIG_THEATRE) ? NUM_SEATS_PER_ROW_NORMAL_THEATRE : NUM_SEATS_PER_ROW_BIG_THEATRE;
    }
}