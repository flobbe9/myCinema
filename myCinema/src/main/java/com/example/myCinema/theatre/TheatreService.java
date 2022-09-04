package com.example.myCinema.theatre;

import static com.example.myCinema.theatre.Theatre.MAX_ROWS;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.myCinema.CheckEntity;
import com.example.myCinema.theatre.row.Row;
import com.example.myCinema.theatre.row.RowRepository;
import com.example.myCinema.theatre.seat.Seat;
import com.example.myCinema.theatre.seat.SeatRepository;

import lombok.AllArgsConstructor;


/**
 * Contains methods to use endpoints and persist the theatre entity.
 * 
 * <p>Extends CheckEntity for checking objects and collections.
 */
@Service
@AllArgsConstructor
public class TheatreService extends CheckEntity {

    private final TheatreRepository theatreRepository;
    private final RowRepository rowRepository;
    private final SeatRepository seatRepository;
    

    /**
     * Saves new theatre to the db if it's valid and one with the same number doesn't already exist.
     * 
     * @param theatre to add.
     * @return saved theatre.
     */
    public Theatre addNew(Theatre theatre) {

        // checking theatre data
        theatreValid(theatre);
                
        // null values
        hasNullValue(theatre);

        // checking if theatre does already exists
        if (exists(theatre.getNumber())) 
            throw new IllegalStateException("Theatre with number \"" + theatre.getNumber() + "\" does already exist.");

        // setting up rows, seats ect.
        theatre.setFieldVariables();

        return save(theatre);
    }


    public Row saveRow(Row row) {

        return rowRepository.save(row); 
    }
    

    public Seat saveSeat(Seat seat) {

        return seatRepository.save(seat);
    }

    
    /**
     * Tries to update theatre if fields in theatreContainer are not null and valid.
     * Needs id to get original theatre from db.
     * 
     * @param theatreContainer contains new fields that should replace the old ones.
     * @return updated theatre.
     */
    @Transactional
    public Theatre update(Theatre theatreContainer) {

        // checking wether id is null
        if (theatreContainer.getId() == null) 
            throw new IllegalStateException("Id of theatreContainer must not be null.");

        // getting theatre to update from repo
        Theatre theatreToUpdate = getById(theatreContainer.getId());
        
        // number
        if (!objectNullOrEmpty(theatreContainer.getNumber())) theatreToUpdate.setNumber(theatreContainer.getNumber());
        // threeD
        if (!objectNullOrEmpty(theatreContainer.getThreeD())) theatreToUpdate.setThreeD(theatreContainer.getThreeD());
        // rowsTotal
        if (!objectNullOrEmpty(theatreContainer.getRowsTotal())) theatreToUpdate.setRowsTotal(theatreContainer.getRowsTotal());
        
        // checking theatre data
        theatreValid(theatreToUpdate);
        
        // removing rows from db 
        deleteAllRows(theatreToUpdate);
        
        // setting up new rows and seats ect.
        theatreToUpdate.setFieldVariables();

        return save(theatreToUpdate);
    }
    
    
    public Theatre getById(long id) {

        return theatreRepository.findById(id).orElseThrow(() -> 
            new IllegalStateException("Could not find theatre with id \"" + id + "\"."));
    }
    
    
    public Theatre getByNumber(int number) {

        return theatreRepository.findByNumber(number).orElseThrow(() ->
            new IllegalStateException("Could not find theatre with number \"" + number + "\"."));
    }
    
    
    public List<Theatre> getAll() {

        // order by number of theatre, ascending
        return theatreRepository.findAllByOrderByNumberAsc();
    }


    public Row getRow(int theatreNumber, char rowLetter) {

        // getting theatre
        Theatre theatre = getByNumber(theatreNumber); 

        return rowRepository.findByTheatreAndRowLetter(theatre, rowLetter).orElseThrow(() -> 
            new IllegalStateException("Could not find row with theatre number \"" + theatreNumber + "\" and row letter \"" + rowLetter + "\"."));            

    }


    public Seat getSeat(int theatreNumber, char rowLetter, int seatNumber) {

        // getting row
        Row row = getRow(theatreNumber, rowLetter);

        return seatRepository.findByRowAndSeatNumber(row, seatNumber).orElseThrow(() ->
            new IllegalStateException("Could not find seat with theatre number \"" + theatreNumber + "\", row letter \"" + rowLetter + "\" and seat number \"" + seatNumber + "\"."));
    }
    
    
    public void delete(int number) {

        // get theatre by number
        Theatre theatre = getByNumber(number);

        theatreRepository.delete(theatre);
    }
    
    
    public boolean exists(int number) {

        return theatreRepository.findByNumber(number).isPresent();
    }


    public void setSeatTaken(Seat seat, boolean taken) {

        seat.setTaken(taken);

        saveSeat(seat);
    }
    
    
/// helper functions


    private Theatre save(Theatre theatre) {

        return theatreRepository.save(theatre);
    }
    
    
    /**
     * Checks several fields of theatre. Throws exception if one check was unsuccessful.
     * 
     * @param theatre to check.
     * @return true if all checks were successfull.
     */
    private boolean theatreValid(Theatre theatre) {

        // totalRows cannot be over 26
        if (theatre.getRowsTotal() > MAX_ROWS)
            throw new IllegalStateException("Number of rows cannot be over " + MAX_ROWS + ".");

        // totalRows cannot be lower than 1
        if (theatre.getRowsTotal() < 1) 
            throw new IllegalStateException("Number of rows cannot be lower than 1.");

        return true;
    }
    
    
    /**
     * Checks all fields that are not auto generated for null values and empty strings.
     * Throws exception if falsy value is found.
     * 
     * @param theatre to check.
     * @return false if no null value or empty string was found.
     */
    private boolean hasNullValue(Theatre theatre) {

        if (// number
            objectNullOrEmpty(theatre.getNumber()) ||
            // threeD
            objectNullOrEmpty(theatre.getThreeD()) ||
            // rowsTotal
            objectNullOrEmpty(theatre.getRowsTotal()))
            
                throw new IllegalStateException("Theatre data contains null value or empty strings ('').");

        return false;
    }


    private void deleteAllRows(Theatre theatre) {

        rowRepository.deleteByTheatre(theatre);
    }
}