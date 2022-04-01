package com.example.myDiningReview.any;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegistrationRequest {
    private String userName;
    private String password;
    private String email;
    private String city;
    private String state;
    private Integer zipCode;
    private Boolean peanutAllergies;
    private Boolean eggAllergies;
    private Boolean dairyAllergies;
}
