package com.example.myCinema.theatre;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TheatreRepository extends JpaRepository <Theatre, Long>{
    Optional<Theatre> findByNumber(int number);
    List<Theatre> findAllByOrderByNumberAsc();
}