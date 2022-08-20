package com.example.myCinema.theatre;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * For testing purposes, doesn't direct user to html pages.
 * No authorization needed.
 */
@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class TheatreController {
    private final TheatreService theatreService;


    @PostMapping("/addTheatre")
    public Theatre addTheatre(@RequestBody Theatre theatre) {
        return theatreService.addNew(theatre);
    }


    @PutMapping("/updateTheatre")
    public Theatre update(@RequestBody Theatre theatreData) {
        return theatreService.update(theatreData);
    }


    @GetMapping("/getTheatreByNumber")
    public Theatre getByNumber(@RequestParam("number") int number) {
        return theatreService.getByNumber(number);
    }


    @GetMapping("/getAllTheatres")
    public List<Theatre> getAll() {
        return theatreService.getAll();
    }


    @DeleteMapping("/deleteTheatre")
    public void delete(@RequestParam("number") int number) {
        theatreService.delete(number);
    }
}