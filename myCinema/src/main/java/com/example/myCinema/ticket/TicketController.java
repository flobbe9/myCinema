package com.example.myCinema.ticket;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping
@CrossOrigin("http://localhost:4001")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    @PostMapping("/addTicket")
    public Ticket addNew(@RequestBody Ticket ticket) {
        return ticketService.addNew(ticket);
    }


    @GetMapping("/getTicket")
    public Ticket getBySeat(@RequestParam("theatreNumber") int theatreNumber, 
                            @RequestParam("rowLetter") char rowLetter,
                            @RequestParam("seatNumber") int seatNumber) {
        return ticketService.getBySeat(theatreNumber, rowLetter, seatNumber);
    }


    @GetMapping("/getTicketByUserName")
    public List<Ticket> getByUserName(@RequestParam("userName") String userName) {
        return ticketService.getByUserName(userName);   
    }
}