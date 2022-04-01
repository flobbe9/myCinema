package com.example.myLogin.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "user_")
@AllArgsConstructor
public class User {
    @Getter @Id @GeneratedValue private Long id;

    @Getter @Setter private String userName;

    @Getter @Setter private String password;

    @Getter @Setter private String firstName;

    @Getter @Setter private String lastName;

    @Getter @Setter private String birthday;
    
    @Getter @Setter private Long age;

    @Getter @Setter private String mail;

    public User() {
        
    }
}
