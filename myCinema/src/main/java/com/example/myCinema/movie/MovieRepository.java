package com.example.myCinema.movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);
    Optional<Movie> findByTitleAndVersion(String title, MovieVersion version);
    List<Movie> findAllByOrderByLocalReleaseDateDesc();
}