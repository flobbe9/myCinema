package com.example.myCinema.theatre;

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
public class TheatreTemplates extends ExceptionService {
    
    private final TheatreService theatreService;


// addTheatre


    @GetMapping("/addNew")
    public String addNew(Model model) {

        // passing thymeleaf the theatre
        model.addAttribute("theatre", new Theatre());

        return "admin/theatre/addNew";
    }


    @PostMapping("/addNew")
    public String addTheatre(Theatre theatre, Model model) {

        // adding new theatre
        theatreService.addNew(theatre);

        // telling thymeleaf it worked
        model.addAttribute("created", true); 
            
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

        // setting id of theatre
        Theatre existingTheatre = theatreService.getByNumber(theatre.getNumber());
        theatre.setId(existingTheatre.getId());

        // updating theatre
        theatreService.update(theatre);

        // telling thymeleaf it worked
        model.addAttribute("ok", true);

        return "admin/theatre/update";
    }


    @PostMapping("/delete")
    public String delete(Theatre theatre, Model model) {
        
        // getting theatre number from theatre 
        int number = theatre.getNumber();

        // deleting theatre
        theatreService.delete(number);

        // telling thymeleaf it worked
        model.addAttribute("gone", true);

        return "admin/theatre/update";
    }
}