package com.example.myCinema.theatre;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.OK;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.AllArgsConstructor;


@Controller
@RequestMapping("/admin/theatre")
@AllArgsConstructor
public class TheatreTemplates {
    private final TheatreService theatreService;
    private final ExceptionService exceptionService;


// addTheatre


    @GetMapping("/addNew")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public String addNew(Model model) {
        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "admin/theatre/addNew";
    }


    @PostMapping("/addNew")
    // @PreAuthorize("hasRole('ROLE_USER')")
    public String addTheatre(Theatre theatre, Model model) {
        try {
            // adding new theatre
            theatreService.addNew(theatre);

            // telling thymeleaf it worked
            model.addAttribute("httpStatus", CREATED); 
            
        } catch(Exception e) {
            // passing exceptions and https status to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

        return "admin/theatre/addNew";
    }  


// updateTheatre


    @GetMapping("/update")
    public String update(Model model) {
        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "admin/theatre/update";
    }


    @PostMapping("/update")
    public String update(Theatre theatre, Model model) {
        try {
            // setting id of theatre
            Theatre existingTheatre = theatreService.getByNumber(theatre.getNumber());
            theatre.setId(existingTheatre.getId());

            // updating theatre
            theatreService.update(theatre);

            // telling thymeleaf it worked
            model.addAttribute("httpStatus", OK);

        } catch (Exception e) {
            // passing exception to thymeleaf error page
            return exceptionService.passExceptionToThymeleaf(e, model);
        }
        return "admin/theatre/update";
    }


    @PostMapping("/delete")
    public String delete(Theatre theatre, Model model) {
        try {
            // getting theatre number from theatre 
            int number = theatre.getNumber();

            // deleting theatre
            theatreService.delete(number);

            // telling thymeleaf it worked
            model.addAttribute("httpStatus", GONE);

        } catch (Exception e) {
            // passing exception to thymeleaf
            return exceptionService.passExceptionToThymeleaf(e, model);
        }

        return "admin/theatre/update";
    }
}