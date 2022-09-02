package com.example.myCinema.theatre;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myCinema.exception.ExceptionService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/theatre")
@RequiredArgsConstructor
public class TheatreTemplates extends ExceptionService {
    
    private final TheatreService theatreService;
    private Long theatreId;


// addTheatre


    @GetMapping("/addNew")
    public String addNew(Model model) {

        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "admin/theatre/addNew";
    }


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


    @PostMapping("/update")
    public String update(Theatre theatre, Model model) {

        try {
            // setting id of theatre
            theatre.setId(theatreId);

            // updating theatre
            theatreService.update(theatre);

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


    @PostMapping("/delete")
    public String delete(Theatre theatre, Model model) {

        try {
            // getting theatre number from theatre 
            int number = theatre.getNumber();

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