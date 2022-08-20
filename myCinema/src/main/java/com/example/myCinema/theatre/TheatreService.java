package com.example.myCinema.theatre;

import static com.example.myCinema.theatre.Theatre.NUM_ROWS_FOR_BIG_CINEMA;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.myCinema.theatre.row.Row;
import com.example.myCinema.theatre.row.RowRepository;
import com.example.myCinema.theatre.seat.Seat;
import com.example.myCinema.theatre.seat.SeatRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TheatreService {
    private final TheatreRepository theatreRepository;
    private final RowRepository rowRepository;
    private final SeatRepository seatRepository;
    

    public Theatre addNew(Theatre theatre) {
        // checking theatre data
        if (!check(theatre) || exists(theatre.getNumber())) {
            throwIllegalState("Something wrong with theatre input data.");
        }

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

    
    @Transactional
    public Theatre update(Theatre theatreData) {
        // checking wether id is null
        if (theatreData.getId() == null) {
            throwIllegalState("Id of theatreData must not be null.");
        }

        // getting theatre to update from repo
        Theatre updatedTheatre = getById(theatreData.getId());

        // updating not null values from theatreData
        // number
        if (theatreData.getNumber() != null) updatedTheatre.setNumber(theatreData.getNumber());
        // threeD
        if (theatreData.getThreeD() != null) updatedTheatre.setThreeD(theatreData.getThreeD());
        // rowsTotal
        if (theatreData.getRowsTotal() != null) updatedTheatre.setRowsTotal(theatreData.getRowsTotal());
        
        // removing rows from db 
        deleteAllRows(updatedTheatre);

        // setting up new rows and seats ect.
        updatedTheatre.setFieldVariables();

        return save(updatedTheatre);
    }
    
    
    public Theatre getById(long id) {
        return theatreRepository.findById(id).orElseThrow(() -> 
            new IllegalStateException("Could not find theatre with id " + id + "."));
    }
    
    
    public Theatre getByNumber(int number) {
        return theatreRepository.findByNumber(number).orElseThrow(() ->
            new IllegalStateException("Could not find theatre with number " + number + "."));
    }
    
    
    public List<Theatre> getAll() {
        // order by number of theatre
        return theatreRepository.findAllByOrderByNumberAsc();
    }


    public Row getRow(int theatreNumber, char rowLetter) {
        // getting theatre
        Theatre theatre = getByNumber(theatreNumber); 

        return rowRepository.findByTheatreAndRowLetter(theatre, rowLetter).orElseThrow(() -> 
            new IllegalStateException("Could not find row with theatre number " + theatreNumber + " and rowLetter " + rowLetter + "."));            

    }


    public Seat getSeat(int theatreNumber, char rowLetter, int seatNumber) {
        // getting row
        Row row = getRow(theatreNumber, rowLetter);

        return seatRepository.findByRowAndSeatNumber(row, seatNumber).orElseThrow(() ->
            new IllegalStateException("Could not find seat with theatreNumber " + theatreNumber + ", rowLetter " + rowLetter + " and seatNumber" + seatNumber + "."));
    }
    
    
    public void delete(int number) {
        // get theatre by number
        Theatre theatre = getByNumber(number);

        theatreRepository.delete(theatre);
    }
    
    
    public boolean exists(int number) {
        // getting theatre by number
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
    
    
    private boolean check(Theatre theatre) {
        // checking for null fields
        if (!checkNullValues(theatre)) return false;

        // totalRows cannot be over 26
        if (theatre.getRowsTotal() > NUM_ROWS_FOR_BIG_CINEMA) return false;
        
        return true;
    }
    
    
    private boolean checkNullValues(Theatre theatre) {
        // number
        if (theatre.getNumber() == null) return false;
        // threeD
        if (theatre.getThreeD() == null) return false;
        // rowsTotal
        if (theatre.getRowsTotal() == null)  return false;

        return true;
    }


    private void deleteAllRows(Theatre theatre) {
        rowRepository.deleteByTheatre(theatre);
    }


    private void throwIllegalState(String message) {
        throw new IllegalStateException(message);
    }
}