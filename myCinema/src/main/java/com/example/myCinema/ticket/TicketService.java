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

import com.example.myCinema.CheckEntity;
import com.example.myCinema.movie.Movie;
import com.example.myCinema.movie.MovieService;
import com.example.myCinema.theatre.TheatreService;
import com.example.myCinema.theatre.row.Row;
import com.example.myCinema.theatre.seat.Seat;
import com.example.myCinema.user.AppUserService;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TicketService extends CheckEntity {
    private final TicketRepository ticketRepository;
    private final AppUserService appUserService;
    private final TheatreService theatreService;
    private final MovieService movieService;
    

    public Ticket addNew(Ticket ticket) {
        // check ticket data
        ticketValid(ticket);

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


    private boolean ticketValid(Ticket ticket) {
        // checking if seat is taken
        Seat seat = theatreService.getSeat(ticket.getTheatreNumber(),
                                           ticket.getRowLetter(),
                                           ticket.getSeatNumber());

        // checking if seat is taken
        if (seat.getTaken()) 
            throw new IllegalStateException("Seat " + seat.getSeatNumber() + " in row " + seat.getRowLetter() + " already taken.");

        // checking if appUser exists 
        appUserService.getByUserName(ticket.getUserName());

        // checking if movie exists
        movieService.getByTitle(ticket.getMovieTitle());
        
        // checking null values
        hasNullValue(ticket);

        return true;
    }


    private boolean hasNullValue(Ticket ticket) {
        if (
            // userName
            objectNullOrEmpty(ticket.getUserName()) || 
            // movieTitle
            objectNullOrEmpty(ticket.getMovieTitle()) ||
            // movieVersion
            objectNullOrEmpty(ticket.getMovieVersion()) ||
            // theatreNumber
            objectNullOrEmpty(ticket.getTheatreNumber()) ||
            // rowLetter
            objectNullOrEmpty(ticket.getRowLetter()) ||
            // seatNumber
            objectNullOrEmpty(ticket.getSeatNumber()) ||
            // discount
            objectNullOrEmpty(ticket.getDiscount()) ||
            // date
            objectNullOrEmpty(ticket.getDate()) ||
            // startingTime
            objectNullOrEmpty(ticket.getStartingTime()))
        
                throw new IllegalStateException("Ticket data contains null value or empty strings ('').");

        return false;
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