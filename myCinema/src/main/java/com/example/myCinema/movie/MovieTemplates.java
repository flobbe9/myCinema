package com.example.myCinema.movie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/movie")
@RequiredArgsConstructor
public class MovieTemplates {
    private final MovieService movieService;
    private final ExceptionService exceptionService;

    /** used for update method */
    private Long appUserId;


// addNew


    @GetMapping("/addNew")
    public String addNew(Model model) {
        // passing movie to thymeleaf
        model.addAttribute("movie", new Movie());

        // passing MovieWrapper to thymeleaf
        model.addAttribute("movieWrapper", new MovieWrapper());

        return "/admin/movie/addNew";
    }


    @PostMapping("/addNew")
    public String addNew(Movie movie, MovieWrapper movieWrapper, Model model) {
        try {
            // setting genres array with 'toggledGenres' property of MovieWrapper
            movie.setGenres(iterateToggledGenres(movieWrapper));

            // setting cast and converting from array to Set
            movie.setCast(new HashSet<String>(Arrays.asList(movieWrapper.getMovieCast())));

            // adding new movie
            movieService.addNew(movie);
            
            // telling thymeleaf it worked
            model.addAttribute("created", true);

            // passing new movieWrapper to thymeleaf for new add
            model.addAttribute("movieWrapper", new MovieWrapper());

        } catch (Exception e) {
            // passing exception to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

        return "/admin/movie/addNew";
    }
    
    
// update

    
    @GetMapping("/update")
    public String update(Model model) {
        // passing movie to thymeleaf
        model.addAttribute("movieContainer", new Movie());

        // passing movieWrapper to thymeleaf
        model.addAttribute("movieWrapper", new MovieWrapper());

        return "/admin/movie/update_getByTitleAndVersion";
    }
    

    @PostMapping("/update_getByTitleAndVersion")
    public String checkMovieExists(Movie movieContainer, MovieWrapper movieWrapper, Model model) {
        try {
            // checking if movie exists and setting appUserId
            appUserId = movieService.getByTitleAndVersion(movieContainer.getTitle(), movieContainer.getVersion()).getId();

            // resetting title and version so they are not displayed in thymeleaf
            movieContainer.setTitle(null);
            movieContainer.setVersion(null);

            // passing movie to thymeleaf
            model.addAttribute("movieContainer", movieContainer);

            // passing movieWrapper to thymeleaf
            model.addAttribute("movieWrapper", new MovieWrapper());
            
        } catch (Exception e) {
            // passing exception to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }
        
        return "/admin/movie/update";
    }
    
    
    @PostMapping("/update")
    public String upadte(Movie movieContainer, MovieWrapper movieWrapper, Model model) {
        try {
            // setting id
            movieContainer.setId(appUserId);

            // setting genres array with 'toggledGenres' property of MovieWrapper
            movieContainer.setGenres(iterateToggledGenres(movieWrapper));

            // setting cast and converting from array to set
            movieContainer.setCast(new HashSet<String>(Arrays.asList(movieWrapper.getMovieCast())));

            // updating movie
            movieService.update(movieContainer);

            // telling thymeleaf it worked
            model.addAttribute("created", true);

            // TODO: should work without it...
            model.addAttribute("movieContainer", movieContainer);

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