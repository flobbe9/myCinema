package com.example.myCinema.movie;

import java.util.List;

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
 * Contains enpoint mappings for the movie entity. For testing purposes.
 * Can only be used by a User with role 'ADMIN'.
 */
@RestController
@RequestMapping("/test/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;


//// testing
    
    
    @PostMapping("/addNew")
    public Movie addNew(@RequestBody Movie movie) {

        return movieService.addNew(movie);
    }


    @PutMapping("/update")
    public Movie update(@RequestBody Movie updatedMovie) {

        return movieService.update(updatedMovie);
    }


    @GetMapping("/getByTitle")
    public List<Movie> getByTitle(@RequestParam("title") String title) {

        return movieService.getByTitle(title);
    }


    @GetMapping("/getAll")
    public List<Movie> getAll() {

        return movieService.getAll();
    }


    @DeleteMapping("/delete") 
    public void delete(@RequestParam("title") String title, @RequestParam("version") MovieVersion version) {

        movieService.delete(title, version);
    }


    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        
        movieService.deleteAll();
    }
}