package com.example.myCinema.theatre.seat;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myCinema.theatre.row.Row;


@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Optional<Seat> findByRowAndSeatNumber(Row row, int seatNumber);
}