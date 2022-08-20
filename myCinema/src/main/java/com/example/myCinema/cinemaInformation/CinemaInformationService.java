package com.example.myCinema.cinemaInformation;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.myCinema.mail.EmailValidator;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CinemaInformationService {
    private final CinemaInformationRepository cinemaInformationRepository;


    public CinemaInformation addNew(CinemaInformation cinemaInformation) {
        // checking cinemaInformation
        if (!check(cinemaInformation) || exists(cinemaInformation.getName(), cinemaInformation.getCity())) {
            throw new IllegalStateException("Something wrong with cinemaInformation.");
        }

        return save(cinemaInformation);
    }


    public CinemaInformation update(CinemaInformation cinemaInformationData) {
        // checking if id is null
        if (cinemaInformationData.getId() == null) {
            throw new IllegalStateException("Id of cinemaInformationData must not be null.");
        }

        // creating cinemaInformation with given id
        CinemaInformation updatedCinemaInformation = getById(cinemaInformationData.getId());

        // updating notNull values from cinemaInformationData
        // name
        if (cinemaInformationData.getName() != null) updatedCinemaInformation.setName(cinemaInformationData.getName());
        // city
        if (cinemaInformationData.getCity() != null) updatedCinemaInformation.setCity(cinemaInformationData.getCity());
        // zipCode
        if (cinemaInformationData.getZipCode() != null) updatedCinemaInformation.setZipCode(cinemaInformationData.getZipCode());
        // adress
        if (cinemaInformationData.getAdress() != null) updatedCinemaInformation.setAdress(cinemaInformationData.getAdress());
        // email 
        if (cinemaInformationData.getEmail() != null) updatedCinemaInformation.setEmail(cinemaInformationData.getEmail());
        // phoneNumber
        if (cinemaInformationData.getPhoneNumber() != null) updatedCinemaInformation.setPhoneNumber(cinemaInformationData.getPhoneNumber());

        return save(updatedCinemaInformation);
    }


    public CinemaInformation getById(Long id) {
        return cinemaInformationRepository.findById(id).orElseThrow(() ->   
            new NoSuchElementException("Could not find cinemaInformation with id \"" + id + "\"."));
    }


    public CinemaInformation getByNameAndCity(String name, String city) {
        return cinemaInformationRepository.findByNameAndCity(name, city).orElseThrow(() -> 
            new NoSuchElementException("Could not find cinema with name \"" + name + "\" and city \"" + city + "\"."));
    }


    public void delete(String name, String city) {
        // getting cinemaInformation by name and city
        CinemaInformation cinemaInformation = getByNameAndCity(name, city);
        
        cinemaInformationRepository.delete(cinemaInformation);
    }
    
    
/// helper functions


    private CinemaInformation save(CinemaInformation cinemaInformation) {
        return cinemaInformationRepository.save(cinemaInformation);
    }
    
    
    private boolean exists(String name, String city) {
        // finding by name and city
        return cinemaInformationRepository.findByNameAndCity(name, city).isPresent();
    }
    

    private boolean check(CinemaInformation cinemaInformation) {
        // checking for null values
        if (!checkNullValues(cinemaInformation)) return false;

        // checking for valid email
        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.validate(cinemaInformation.getEmail())) return false;
        
        return true;
    }
    
    
    private boolean checkNullValues(CinemaInformation cinemaInformation) {
        // name
        if (cinemaInformation.getName() == null) return false;
        // city
        if (cinemaInformation.getCity() == null) return false;
        // zipCode
        if (cinemaInformation.getZipCode() == null) return false;
        // adress
        if (cinemaInformation.getAdress() == null) return false;
        // email
        if (cinemaInformation.getEmail() == null) return false;
        // phoneNumber
        if (cinemaInformation.getPhoneNumber() == null) return false;

        return true;
    }
}