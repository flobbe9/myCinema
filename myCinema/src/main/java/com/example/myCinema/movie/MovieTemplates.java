package com.example.myCinema.movie;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/admin/movie")
@AllArgsConstructor
public class MovieTemplates {
    private final MovieService movieService;
    private final ExceptionService exceptionService;


// addNew


    @GetMapping("/addNew")
    public String addNew(Model model) {
        // passing movie to thymeleaf
        model.addAttribute("movie", new Movie());

        // passing MovieWrapper to thymeleaf
        MovieWrapper movieWrapper = new MovieWrapper();
        model.addAttribute("movieWrapper", movieWrapper);

        return "/admin/movie/addNew";
    }


    // TODO: THYMELEAF: check wether at least one genre checkbox is toggled
    @PostMapping("/addNew")
    public String addNew(Movie movie, MovieWrapper movieWrapper, Model model) {
        try {
            // setting genres array with 'toggledGenres' property of MovieWrapper
            movie.setCast(movieWrapper.getCast());
            movie.setGenres(iterateToggledGenres(movieWrapper));
            
            // adding new movie
            movieService.addNew(movie);
            
            // telling thymeleaf it worked
            model.addAttribute("httpStatus", CREATED);
            
        } catch (Exception e) {
            // passing exception to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

        return "/admin/movie/addNew";
    }
    
    
    // update
    
    
    // TODO: THYMELEAF: empty text is interpreted as '' instead of null, should send null on submit
    @GetMapping("/update")
    public String update(Model model) {
        // passing movie to thymeleaf
        model.addAttribute("movie", new Movie());

        // passing MovieWrapper to thymeleaf
        MovieWrapper movieWrapper = new MovieWrapper();
        model.addAttribute("movieWrapper", movieWrapper);

        return "/admin/movie/update";
    }


    @PostMapping("/update")
    public String upadte(Movie movie, MovieWrapper movieWrapper, Model model) {
        try {
            // setting movie id
            Long id = movieService.getByTitleAndVersion(movie.getTitle(), movie.getVersion()).getId();
            movie.setId(id);

            // setting genres array with 'toggledGenres' property of MovieWrapper
            movie.setCast(movieWrapper.getCast());
            movie.setGenres(iterateToggledGenres(movieWrapper));

            // updating movie
            movieService.update(movie);

            // telling thymeleaf it worked
            model.addAttribute("httpStatus", CREATED);

        } catch (Exception e) {
            // passing exception to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

        return "/admin/movie/update";
    }


//// helper functions


    private Set<Genre> iterateToggledGenres(MovieWrapper movieWrapper) {
        Genre[] genresArr = movieWrapper.getGenres();
        boolean[] toggledGenres = movieWrapper.getToggledGenres();

        Set<Genre> genres = new HashSet<Genre>();

        for (int i = 0; i < genresArr.length; i++) {
            // if toggled, then add
            if (toggledGenres[i]) {
                genres.add(genresArr[i]);
            }
        }

        return genres;
    }
}