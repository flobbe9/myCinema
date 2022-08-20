package com.example.myCinema.cinemaInformation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CinemaInformationRepository extends JpaRepository<CinemaInformation, Long> {
    Optional<CinemaInformation> findByNameAndCity(String name, String city);
}