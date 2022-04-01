package com.example.myLogin.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.example.myLogin.model.User;
import com.example.myLogin.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/myLogin")
public class LoginController {
    private UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @PostMapping("/login")
    public User checkLogin(@RequestBody User user) {
        Optional<User> optionalUserName = userRepository.findByUserName(user.getUserName());

        if (!optionalUserName.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user with this user name.");
        }
        User newUser = optionalUserName.get();

        if (newUser.getPassword().equals(user.getPassword())) {
            return newUser;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Wrong password.");
        }
    }

    @PostMapping("/add") 
    public User addUser(@RequestBody User newUser) {
        Optional<User> optionalUserName = userRepository.findByUserName(newUser.getUserName());
        Optional<User> optionalUserMail = userRepository.findByMail(newUser.getMail());

        // checking for douplicate mail and userName
        if (optionalUserMail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.IM_USED, "Mail does already exist.");
        }
        if (optionalUserName.isPresent()) {
            throw new ResponseStatusException(HttpStatus.IM_USED, "Username does already exist."); 
        }
        // setting age with birthday 
        if (newUser.getBirthday() != null) {
            LocalDate today = LocalDate.now();
            String birthday = newUser.getBirthday();

            String bYear = birthday.substring(0, 4);
            String bMonth = birthday.substring(5, 7);
            String bDay = birthday.substring(8, 10);

            LocalDate b = LocalDate.parse(bYear + "-" + bMonth + "-" + bDay);

            newUser.setAge(ChronoUnit.YEARS.between(b, today));
        }
        
        return userRepository.save(newUser);
    }
}