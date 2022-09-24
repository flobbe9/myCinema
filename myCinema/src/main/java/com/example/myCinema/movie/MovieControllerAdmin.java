package com.example.myCinema.movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


/**
 * Controller for admins. Contains endpoints to alter movies, delete or add one. Methods work with thymeleaf templates.
 * Can only be accessed by user with role 'ADMIN'.
 */
@Controller
@RequestMapping("/admin/movie")
@RequiredArgsConstructor
public class MovieControllerAdmin {
    
    private final MovieService movieService;
    
    /** Used for {@link #update(Model)}. */
    private Long movieId;


// addNew


    @GetMapping("/addNew")
    public String addNew(Model model) {

        // passing movie to thymeleaf
        model.addAttribute("movie", new Movie());

        // passing MovieWrapper to thymeleaf
        model.addAttribute("movieWrapper", new MovieWrapper());

        return "admin/movie/addNew";
    }


    /**
     * Tries to add the movie and passes confirmation boolean to page if successfull.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param movie to add.
     * @param movieWrapper for helping the html page.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/addNew")
    public String addNew(Movie movie, MovieWrapper movieWrapper, Model model) {

        try {
            // setting genres array with 'toggledGenres' field of movieWrapper
            movie.setGenres(iterateToggledGenres(movieWrapper));

            // setting versions array with 'toggledVersions' field of movieWrapper
            movie.setVersions(iterateToggledVersions(movieWrapper));

            // setting cast and converting from array to Set
            movie.setCast(Arrays.asList(movieWrapper.getMovieCast()));

            // adding new movie
            movieService.addNew(movie);
            
            // telling thymeleaf it worked
            model.addAttribute("created", true);

        } catch (Exception e) {
            // passing exception message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("movieWrapper", new MovieWrapper());

        return "admin/movie/addNew";
    }
    
    
// update


@GetMapping("/update")
    public String update(Model model) {

        // passing movie to thymeleaf
        model.addAttribute("movieContainer", new Movie());

        model.addAttribute("movieWrapper", new MovieWrapper());

        return "admin/movie/update_getByTitle";
    }
    

    /**
     * If movie exists the user is redirected to the update page. {@link #movieId} is set for the update method.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param movieContainer contains title of movie to be updated.
     * @param movieWrapper for helping the html page.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/update_getByTitle")
    public String checkMovieExists(Movie movieContainer, Model model) {
        
        try {
            // checking if movie exists
            Movie movie = movieService.getByTitle(movieContainer.getTitle());
            
            // setting movie id
            movieId = movie.getId();
            
            // setting movie cast for thymeleaf placeholders
            MovieWrapper movieWrapper = new MovieWrapper();
            movieWrapper.setMovieCast(movie.getCast().toArray(movieWrapper.getMovieCast()));
            
            // passing movieWrapper to thymeleaf
            model.addAttribute("movieWrapper", movieWrapper);
            
            // passing movie to thymeleaf
            model.addAttribute("movieContainer", movieContainer);
            
        } catch (Exception e) {
            // passing exception message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
            
            return "/admin/movie/update_getByTitle";
        }
        
        return "admin/movie/update";
    }
    
    
    /**
     * Actually tries to update the movie and passes confirmation boolean to page if successfull.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param movieContainer contains new data that should be used as replacment.
     * @param movieWrapper for helping the html page.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/update")
    public String update(Movie movieContainer, MovieWrapper movieWrapper, Model model) {

        try {
            // setting id
            movieContainer.setId(movieId);

            // setting genres set with 'toggledGenres' field of MovieWrapper
            movieContainer.setGenres(iterateToggledGenres(movieWrapper));

            // setting versions with 'toggledVersions' field of MovieWrapper
            movieContainer.setVersions(iterateToggledVersions(movieWrapper));

            // setting cast and converting from array to list
            movieContainer.setCast(new ArrayList<String>(Arrays.asList(movieWrapper.getMovieCast())));

            // updating movie
            movieService.update(movieContainer);

            // telling thymeleaf it worked
            model.addAttribute("ok", true);

        } catch (Exception e) {
            // passing exception to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        model.addAttribute("movieContainer", movieContainer);    

        model.addAttribute("movieWrapper", new MovieWrapper());

        return "admin/movie/update";
    }


//// helper functions


    /**
     * Iterates toggled genres. If true value is found the genre at the same index in genresArr is added to the genres set.
     * 
     * @param movieWrapper contains array with booleans representing which genres were checked.
     * @return set with actual genres that were selected.
     */
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
    

    /**
     * Iterates toggled versions. If true value is found the genre at the same index in versionsArr is added to the versions set.
     * 
     * @param movieWrapper contains array with booleans representing which versions were checked.
     * @return set with actual versions that were selected.
     */
    private Set<MovieVersion> iterateToggledVersions(MovieWrapper movieWrapper) {

        MovieVersion[] versionsArr = movieWrapper.getVersions();
        boolean[] toggledVersions = movieWrapper.getToggledVersions();

        Set<MovieVersion> versions = new HashSet<>();

        for (int i = 0; i < versionsArr.length; i++) {
            // if toggled, then add
            if (toggledVersions[i]) {
                versions.add(versionsArr[i]);
            }
        }

        return versions;
    }
}