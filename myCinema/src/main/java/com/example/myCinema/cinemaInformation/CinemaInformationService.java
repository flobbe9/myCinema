package com.example.myCinema.cinemaInformation;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.myCinema.CheckEntity;
import com.example.myCinema.mail.EmailValidator;

import lombok.AllArgsConstructor;


/** 
 * Contains methods to use endpoints and persist CinemaInformation.
 * 
 * <p>Extends CheckEntity for checking objects and collections.
 */
@Service
@AllArgsConstructor
public class CinemaInformationService extends CheckEntity {
    
    private final CinemaInformationRepository cinemaInformationRepository;
    
    /**
     * Validates new cinemaInformation and checks whether this cinema does already exist.
     * If no exceptions are thrown the new cinema is saved in db.
     * 
     * @param cinemaInformation to add.
     * @return saved cinemaInformation.
     */
    public CinemaInformation addNew(CinemaInformation cinemaInformation) {

        // checking cinemaInformation
        cinemaInformationValid(cinemaInformation);

        // checking for null values
        hasNullValue(cinemaInformation);

        // checking if cinema already exists
        if (exists(cinemaInformation.getName(), cinemaInformation.getCity())) 
            throw new IllegalStateException("Cinema with name \"" + cinemaInformation.getName() + "\" in city \"" + cinemaInformation.getCity() + "\" does already exists.");
        
        return save(cinemaInformation);
    }
    
    
    /**
     * Updates cinemaInformation beeing able to change all fields except the id.
     * Takes a cinemaInformation object and checks every field of it for new values.
     * This object has to contain the id of the cinema to be updated, so it can be found in the db.
     * 
     * <p>Checks fields for null values and emnpty strings. Validates email.
     * 
     * <p>Throws exception if validation is unsuccessful or id is null.
     * 
     * @param cinemaInformationContainer contains new fields of cinema.
     * @return updated cinema.
     */
    public CinemaInformation update(CinemaInformation cinemaInformationContainer) {
        
        // checking cinemaInformation
        cinemaInformationValid(cinemaInformationContainer);

        // checking if id is null
        if (cinemaInformationContainer.getId() == null) 
            throw new IllegalStateException("Id of cinemaInformationContainer must not be null.");
        
        // creating cinemaInformation with given id
        CinemaInformation cinemaInformationToUpdate = getById(cinemaInformationContainer.getId());

        // name
        if (!objectNullOrEmpty(cinemaInformationContainer.getName())) cinemaInformationToUpdate.setName(cinemaInformationContainer.getName());
        // city
        if (!objectNullOrEmpty(cinemaInformationContainer.getCity())) cinemaInformationToUpdate.setCity(cinemaInformationContainer.getCity());
        // zipCode
        if (!objectNullOrEmpty(cinemaInformationContainer.getZipCode())) cinemaInformationToUpdate.setZipCode(cinemaInformationContainer.getZipCode());
        // adress
        if (!objectNullOrEmpty(cinemaInformationContainer.getAdress())) cinemaInformationToUpdate.setAdress(cinemaInformationContainer.getAdress());
        // email 
        if (!objectNullOrEmpty(cinemaInformationContainer.getEmail())) cinemaInformationToUpdate.setEmail(cinemaInformationContainer.getEmail());
        // phoneNumber
        if (!objectNullOrEmpty(cinemaInformationContainer.getPhoneNumber())) cinemaInformationToUpdate.setPhoneNumber(cinemaInformationContainer.getPhoneNumber());

        return save(cinemaInformationToUpdate);
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
    

    /**
     * Validate fields of cinemaInformation.
     * 
     * @param cinemaInformation to check.
     * @return true if all checks were successfull.
     */
    private boolean cinemaInformationValid(CinemaInformation cinemaInformation) {

        EmailValidator emailValidator = new EmailValidator();

        // checking for valid email
        return emailValidator.validate(cinemaInformation.getEmail());
    }
    
    
    /**
     * Checks every field for null values or empty strings. 
     * 
     * <p> Throws exception if one is found.
     * 
     * @param cinemaInformation to check.
     * @return false if cinemaInformation has no null value or empty strings.
     */
    private boolean hasNullValue(CinemaInformation cinemaInformation) {

        if (// name
            objectNullOrEmpty(cinemaInformation.getName()) ||
            // city
            objectNullOrEmpty(cinemaInformation.getCity()) ||
            // zipCode
            objectNullOrEmpty(cinemaInformation.getZipCode()) ||
            // adress
            objectNullOrEmpty(cinemaInformation.getAdress()) ||
            // email
            objectNullOrEmpty(cinemaInformation.getEmail()) ||
            // phoneNumber
            objectNullOrEmpty(cinemaInformation.getPhoneNumber()))

                throw new IllegalStateException("Cinema information contains null values or empty strings ('').");

        return false;
    }
}