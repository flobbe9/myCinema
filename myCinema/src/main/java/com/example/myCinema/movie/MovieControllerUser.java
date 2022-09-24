package com.example.myCinema.movie;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/movie")
@AllArgsConstructor
public class MovieControllerUser {
    
    private final MovieService movieService;


    @GetMapping("/getAll")
    public String getAll(Model model) {

        // putting all movies into the movieWrapper
        MovieWrapper movieWrapper = new MovieWrapper();
        movieWrapper.setMovies(movieService.getAll());

        // adding list of movies to thymeleaf
        model.addAttribute("movieWrapper", movieWrapper);

        return "user/movie/getAll";
    }


    @GetMapping("/getByTitle")
    public String getByTitle(MovieWrapper movieContainer, Model model) {

        try {
            // getting title from movieContainer
            String title = "";
            for (Movie movie : movieContainer.getMovies()) {
                if (movie.getTitle() != null) {
                    title = movie.getTitle();
                    break;
                } 
            }

            // getting movie from db
            Movie movie = movieService.getByTitle(title);

            // passing movie to thymeleaf
            model.addAttribute("movie", movie);

            // passing movieWrapper to thymeleaf
            model.addAttribute("movieWrapper", new MovieWrapper());

        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
            // TODO: how to handle exceptions?
        }

        return "user/movie/getByTitle";
    }
}