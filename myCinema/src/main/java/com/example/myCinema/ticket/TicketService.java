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
import com.example.myCinema.appUser.AppUser;
import com.example.myCinema.appUser.AppUserService;
import com.example.myCinema.mail.MailService;
import com.example.myCinema.movie.Movie;
import com.example.myCinema.movie.MovieService;
import com.example.myCinema.theatre.TheatreService;
import com.example.myCinema.theatre.row.Row;
import com.example.myCinema.theatre.seat.Seat;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;


/**
 * Contains methods to use endpoints and persist the ticket entity.
 * 
 * <p>Extends CheckEntity for checking objects and collections.
 */
@Service
@AllArgsConstructor
public class TicketService extends CheckEntity {
    
    private final TicketRepository ticketRepository;
    private final AppUserService appUserService;
    private final TheatreService theatreService;
    private final MovieService movieService;
    private final MailService mailService;
    

    /**
     * Adds new ticket to db. Checks all fields and sets seat as taken. Should also send an email 
     * to the user with all neccessary information.
     * 
     * <p> All paths are relative to the 'mail' folder, not to the 'ticket' folder.
     * 
     * @param ticket to add.
     * @return saved ticket.
     */
    public Ticket addNew(Ticket ticket) {
        
        // check ticket data
        ticketValid(ticket);
                
        // checking null values
        hasNullValue(ticket);

        // setting ticketData
        setTicketData(ticket);

        // set seat as taken
        Seat seat = theatreService.getSeat(ticket.getTheatreNumber(), ticket.getRowLetter(), ticket.getSeatNumber());
        theatreService.setSeatTaken(seat, true);

        // getting email text from html file
        String emailPath = "./html/ticketConfirmationEmail.html";

        // sending ticket confirmation email with ticket pdf
        mailService.send(emailPath, 
                         getTicketConfirmationEmailFillInList(ticket),
                         ticket.getEmail(), 
                         null); // TODO: send request to different api to create Ticket.pdf from ./html/Ticket.html

        return ticketRepository.save(ticket);
    }


    public Ticket getById(Long id) {

        return ticketRepository.findById(id).orElseThrow(() -> 
            new NoSuchElementException("Could not find ticket with id \"" + id + "\"."));
    }


    public List<Ticket> getByEmail(String email) {

        return ticketRepository.findAllByEmail(email);
    }


    public Ticket getBySeat(int theatreNumber, char rowLetter, int seatNumber) {

        return ticketRepository.findByTheatreNumberAndRowLetterAndSeatNumber(theatreNumber, rowLetter, seatNumber).orElseThrow(() -> 
            new NoSuchElementException("Could not find ticket with theatre number \"" + theatreNumber + "\", row letter \"" + rowLetter + "\" and seat number \"" + seatNumber + "\"."));
    }


    public void delete(int theatreNumber, char rowLetter, int seatNumber) {
        
        // checking if ticket exists
        Ticket ticket = getBySeat(theatreNumber, rowLetter, seatNumber);

        // setting seat back to untaken
        Seat seat = theatreService.getSeat(theatreNumber, rowLetter, seatNumber);
        seat.setTaken(false);

        ticketRepository.delete(ticket);
    }


//// helper functions


    /**
     * Checks some ticket fields. Throws exception if any check is unsuccessful.
     * 
     * @param ticket to check.
     * @return true if nothing wrong with ticket.
     */
    private boolean ticketValid(Ticket ticket) {

        // checking if seat is taken
        Seat seat = theatreService.getSeat(ticket.getTheatreNumber(),
                                           ticket.getRowLetter(),
                                           ticket.getSeatNumber());

        // checking if seat is taken
        if (seat.getTaken()) 
            throw new IllegalStateException("Seat " + seat.getSeatNumber() + " in row " + seat.getRowLetter() + " already taken.");

        // checking if appUser exists 
        appUserService.getByEmail(ticket.getEmail());

        // checking if movie exists
        movieService.getByTitle(ticket.getMovieTitle());
        
        return true;
    }


    /**
     * Checking if ticket fields contain null values or empty strings. Throws exception if one is found.
     * 
     * @param ticket to check.
     * @return false if no null value or empty string is found.
     */
    private boolean hasNullValue(Ticket ticket) {

        if (// email
            objectNullOrEmpty(ticket.getEmail()) || 
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


    /**
     * Setting some fields that can only be set by getting the movie of the ticket.
     * 
     * @param ticket to set fields for.
     */
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
     * Adjusting price depending on the following parameters:
     * 
     * <p>3D:  +2.0$.  
     * <p>RowRank box: +1.5$.  
     * <p>SeatType loveSeat: +1.0$.  
     * <p>Discount student: -1.0$.  
     * <p>Discount child: -1.5$.  
     * 
     * @param ticket which needs a price.
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


    /**
     * Creating list with string variables the ticket confirmation email should be formatted with.
     * 
     * @param ticket contains data for the email.
     * @return list with string variables for the email.
     */
    private List<String> getTicketConfirmationEmailFillInList(Ticket ticket) {

        // getting appUser firstName
        AppUser appUser = appUserService.getByEmail(ticket.getEmail());
        String firstName = appUser.getFirstName();

        //...
        
        return Lists.newArrayList(firstName); // TODO: fill list
    }
}