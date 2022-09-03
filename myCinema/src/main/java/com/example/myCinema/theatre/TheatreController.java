package com.example.myCinema.theatre;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/test/theatre")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
@AllArgsConstructor
public class TheatreController {

    private final TheatreService theatreService;


//// testing


    @PostMapping("/addNew")
    public Theatre addNew(@RequestBody Theatre theatre) {

        return theatreService.addNew(theatre);
    }


    @PutMapping("/update")
    public Theatre update(@RequestBody Theatre theatreData) {

        return theatreService.update(theatreData);
    }


    @GetMapping("/getByNumber")
    public Theatre getByNumber(@RequestParam("number") int number) {

        return theatreService.getByNumber(number);
    }


    @GetMapping("/getAll")
    public List<Theatre> getAll() {

        return theatreService.getAll();
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam("number") int number) {

        theatreService.delete(number);
    }
}