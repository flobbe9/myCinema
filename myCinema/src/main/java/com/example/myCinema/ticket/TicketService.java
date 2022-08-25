package com.example.myCinema.ticket;

import static com.example.myCinema.movie.MovieVersion._3D;
import static com.example.myCinema.theatre.Theatre.BASIC_PRICE;
import static com.example.myCinema.theatre.row.RowRank.BOX;
import static com.example.myCinema.theatre.seat.SeatType.LOVE_SEAT;
import static com.example.myCinema.ticket.Discount.CHILD;
import static com.example.myCinema.ticket.Discount.STUDENT;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.myCinema.movie.Movie;
import com.example.myCinema.movie.MovieService;
import com.example.myCinema.theatre.TheatreService;
import com.example.myCinema.theatre.row.Row;
import com.example.myCinema.theatre.seat.Seat;
import com.example.myCinema.user.AppUserService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final AppUserService appUserService;
    private final TheatreService theatreService;
    private final MovieService movieService;
    


    public Ticket addNew(Ticket ticket) {
        // check ticket
        if (!check(ticket) || exists(ticket.getTheatreNumber(), ticket.getRowLetter(), ticket.getSeatNumber())) {
            throw new IllegalStateException("Something wrong with ticket input data.");
        }

        // setting ticketData
        setTicketData(ticket);

        // set seat as taken
        Seat seat = theatreService.getSeat(ticket.getTheatreNumber(), 
                                           ticket.getRowLetter(), 
                                           ticket.getSeatNumber());
        theatreService.setSeatTaken(seat, true);

        // TODO: send email with pdf ticket to appUser

        return ticketRepository.save(ticket);
    }


    public Ticket getById(Long id) {
        return ticketRepository.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Could not find ticket with id \"" + id + "\"."));
    }


    public List<Ticket> getByUserName(String userName) {
        return ticketRepository.findAllByUserName(userName);
    }


    public Ticket getBySeat(int theatreNumber, char rowLetter, int seatNumber) {
        return ticketRepository.findByTheatreNumberAndRowLetterAndSeatNumber(theatreNumber, rowLetter, seatNumber).orElseThrow(() -> 
            new NoSuchElementException("Could not find ticket with theatreNumber \"" + theatreNumber + "\", rowLetter \"" + rowLetter + "\" and seatNumber \"" + seatNumber + "\"."));
    }


    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }


/// helper functions


    private boolean check(Ticket ticket) {
        // checking null values
        if (!checkNullValues(ticket)) return false;
        
        // checking if movie exists
        if (!movieExists(ticket)) return false;
        
        // checking if appUser exists 
        if (!appUserExists(ticket)) return false;
        
        // checking if seat is taken
        Seat seat = theatreService.getSeat(ticket.getTheatreNumber(),
                                           ticket.getRowLetter(),
                                           ticket.getSeatNumber());
        if (seat.getTaken()) return false;
        
        return true;
    }


    private boolean checkNullValues(Ticket ticket) {
        // userName
        if (ticket.getUserName() == null) return false; 
        // movieTitle
        if (ticket.getMovieTitle() == null) return false;
        // movieVersion
        if (ticket.getMovieVersion() == null) return false;
        // theatreNumber
        if (ticket.getTheatreNumber() == null) return false;
        // rowLetter
        if (ticket.getRowLetter() == null) return false;
        // seatNumber
        if (ticket.getSeatNumber() == null) return false;
        // discount
        if (ticket.getDiscount() == null) return false;
        // date
        if (ticket.getDate() == null) return false;
        // startingTime
        if (ticket.getStartingTime() == null) return false;

        return true;
    }


    private boolean exists(int theatreNumber, char rowLetter, int seatNumber) {
        // find by theatreNumber, rowLetter and seatNumber
        return ticketRepository.findByTheatreNumberAndRowLetterAndSeatNumber(theatreNumber, rowLetter, seatNumber).isPresent();
    }
    
    
    private boolean appUserExists(Ticket ticket) {
        return appUserService.exists(ticket.getUserName());
    }
    
    
    private boolean movieExists(Ticket ticket) {
        return movieService.exists(ticket.getMovieTitle(), ticket.getMovieVersion());
    }


    private void setTicketData(Ticket ticket) {
        // getting seat and row objects with ticket data
        int theatreNumber = ticket.getTheatreNumber();
        char rowLetter = ticket.getRowLetter();
        int seatNumber = ticket.getSeatNumber();

        Seat seat = theatreService.getSeat(theatreNumber, rowLetter, seatNumber);
        Row row = theatreService.getRow(theatreNumber, rowLetter);

        // getting movie object with ticket data
        Movie movie = movieService.getByTitleAndVersion(ticket.getMovieTitle(), ticket.getMovieVersion());

        // setting FSK
        setFSK(ticket, movie);

        // setting rowRank
        setRowRank(ticket, row);

        // setting price
        setPrice(ticket, row, seat);
    }


    private void setFSK(Ticket ticket, Movie movie) {
        ticket.setFsk(movie.getFsk());
    }


    private void setRowRank(Ticket ticket, Row row) {
        // getting row
        ticket.setRowRank(row.getRowRank());
    }


    /**
     * 3D:  +2.0$.  
     * RowRank box: +1.5$.  
     * SeatType loveSeat: +1.0$.  
     * Discount student: -1.0.  
     * Discount child: -1.5.  
     * @param ticket whick needs a price.
     */
    private void setPrice(Ticket ticket, Row row, Seat seat) {
        // setting basic price
        double price = BASIC_PRICE;

        // checking _3D
        if (ticket.getMovieVersion() == _3D) price += 2.0;

        // checking rowRank
        if (row.getRowRank() == BOX) price += 1.5;

        // checking seat type
        if (seat.getSeatType() == LOVE_SEAT) price += 1.0;

        // checking discount
        // student
        if (ticket.getDiscount() == STUDENT) price -= 1.0;
        // child
        if (ticket.getDiscount() == CHILD) price -= 1.5;

        ticket.setPrice(price);
    }
}