package com.example.myCinema.movie;

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


@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;
    
    
    @PostMapping("/addMovie")
    public Movie addNew(@RequestBody Movie movie) {
        return movieService.addNew(movie);
    }


    @PutMapping("/updateMovie")
    public Movie update(@RequestBody Movie updatedMovie) {
        return movieService.update(updatedMovie);
    }


    @GetMapping("/getMovieByTitle")
    public List<Movie> getByTitle(@RequestParam("title") String title) {
        return movieService.getByTitle(title);
    }


    @GetMapping("/getAllMovies")
    public List<Movie> getAll() {
        return movieService.getAll();
    }


    @DeleteMapping("/deleteMovie") 
    public void delete(@RequestParam("title") String title, @RequestParam("version") MovieVersion version) {
        movieService.delete(title, version);
    }
}