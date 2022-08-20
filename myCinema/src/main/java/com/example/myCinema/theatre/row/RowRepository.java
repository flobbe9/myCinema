package com.example.myCinema.theatre.row;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myCinema.theatre.Theatre;


@Repository
public interface RowRepository extends JpaRepository<Row, Long> {
    Optional<Row> findByTheatreAndRowLetter(Theatre theatre, char rowLetter);
    Long deleteByTheatre(Theatre theatre);
}