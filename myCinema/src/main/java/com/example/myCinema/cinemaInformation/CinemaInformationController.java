package com.example.myCinema.cinemaInformation;

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
@RequestMapping("/test/cinemaInformation")
@AllArgsConstructor
public class CinemaInformationController {
    
    private final CinemaInformationService cinemaInformationService;


//// testing


    @PostMapping("/addNew") 
    public CinemaInformation addNew(@RequestBody CinemaInformation cinemaInformation) {

        return cinemaInformationService.addNew(cinemaInformation);
    }


    @PutMapping("/update")
    public CinemaInformation update(@RequestBody CinemaInformation cinemaInformationData) {

        return cinemaInformationService.update(cinemaInformationData);
    }


    @GetMapping("/getByNameAndCity")
    public CinemaInformation getByNameAndCity(@RequestParam("name") String name, @RequestParam("city") String city) {

        return cinemaInformationService.getByNameAndCity(name, city);
    }


    @DeleteMapping("/delete")
    public void delete(@RequestParam("name") String name, @RequestParam("city") String city) {

        cinemaInformationService.delete(name, city);
    }
}