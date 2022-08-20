package com.example.myCinema.ticket;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTheatreNumberAndRowLetterAndSeatNumber(int theatreNumber,
                                                                  char rowLetter,
                                                                  int seatNumber);
    List<Ticket> findAllByUserName(String userName);
    
}