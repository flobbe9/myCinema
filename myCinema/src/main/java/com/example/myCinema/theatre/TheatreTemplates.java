package com.example.myCinema.theatre;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.RequiredArgsConstructor;


/**
 * Controller for admins. Contains endpoints to alter theatres, delete or add one. Methods work with thymeleaf templates.
 * Can only be accessed by user with role 'ADMIN'.
 */
@Controller
@RequestMapping("/admin/theatre")
// @PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class TheatreTemplates extends ExceptionService {
    
    private final TheatreService theatreService;
    
    /** Theatre id for {@link #update(Model)}. */
    private Long theatreId;


// addTheatre


    @GetMapping("/addNew")
    public String addNew(Model model) {

        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "admin/theatre/addNew";
    }


    /** 
     * Tries to add the theatre and passes confirmation boolean to page if successfull.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param theatre to add.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/addNew")
    public String addTheatre(Theatre theatre, Model model) {

        try {
            // adding new theatre
            theatreService.addNew(theatre);

            // telling thymeleaf it worked
            model.addAttribute("created", true);

        } catch (Exception e) {
            // passing exception to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }
            
        return "admin/theatre/addNew";
    }  


// updateTheatre


    @GetMapping("/update")
    public String update(Model model) {

        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "/admin/theatre/update_getByNumber";
    }


    /**
     * If theatre exists the user is redirected to the update page. {@link #theatreId} is set for the update method.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param theatreContainer contains number of theatre to be updated.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @GetMapping("/update_getByNumber")
    public String udpate(Theatre theatreContainer, Model model) {

        try {
            // checking if theatre exists
            Theatre theatre = theatreService.getByNumber(theatreContainer.getNumber());

            // setting theatreId
            theatreId = theatre.getId();

            // passing theatreContainer to thymeleaf
            model.addAttribute("theatre", theatreContainer);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());

            return "/admin/theatre/update_getByNumber";
        }

        return "/admin/theatre/update";
    }


    /**
     * Actually tries to update the theatre and passes confirmation boolean to page if successfull.
     * In any other case the exception is caught and displayed on the same page.
     * 
     * @param theatreContainer contains new data that should be used as replacment.
     * @param movieWrapper for helping the html page.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/update")
    public String update(Theatre theatreContainer, Model model) {

        try {
            // setting id of theatre
            theatreContainer.setId(theatreId);

            // updating theatre
            theatreService.update(theatreContainer);

            // telling thymeleaf it worked
            model.addAttribute("ok", true);

        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin/theatre/update";
    }


// delete


    @GetMapping("/delete")
    public String delete(Model model) {
        
        // passing theatre to thymeleaf
        model.addAttribute("theatre", new Theatre());

        return "/admin/theatre/delete";
    }


    /**
     * Deleting theatre if exists. 
     * 
     * @param theatreContainer with number of theatre to delete.
     * @param model for passing objects to thymeleaf.
     * @return String with html template.
     */
    @PostMapping("/delete")
    public String delete(Theatre theatreContainer, Model model) {

        try {
            // getting theatre number from theatre 
            int number = theatreContainer.getNumber();

            // deleting theatre
            theatreService.delete(number);

            // telling thymeleaf it worked
            model.addAttribute("gone", true);
            
        } catch (Exception e) {
            // passing error message to thymeleaf
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "admin/theatre/delete";
    }
}