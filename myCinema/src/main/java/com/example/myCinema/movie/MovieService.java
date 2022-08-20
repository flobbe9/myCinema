package com.example.myCinema.movie;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;


    @Transactional
    public Movie addNew(Movie movie) {
        // checking movie data
        if (!check(movie) || exists(movie.getTitle(), movie.getVersion())) {
            throw new IllegalStateException("Something wrong with movie input data.");
        }
        
        return save(movie);
    }
    
    
    public Movie update(Movie movieData) {
        // checking if id is null
        if (movieData.getId() == null) {
            throw new IllegalStateException("Id of movieData must not be null.");
        }
        
        // getting movie with given id from repo
        Movie updatedMovie = getById(movieData.getId());
        
        // updating not null values from movieData
        // title
        if (movieData.getTitle() != null) updatedMovie.setTitle(movieData.getTitle());
        // duration
        if (movieData.getDuration() != null) updatedMovie.setDuration(movieData.getDuration());
        // localReleaseDate
        if (movieData.getLocalReleaseDate() != null) updatedMovie.setLocalReleaseDate(movieData.getLocalReleaseDate());
        // localFinishingDate
        if (movieData.getLocalFinishingDate() != null) updatedMovie.setLocalFinishingDate(movieData.getLocalFinishingDate());
        // synopsis
        if (movieData.getSynopsis() != null) updatedMovie.setSynopsis(movieData.getSynopsis());
        // fsk
        if (movieData.getFsk() != null) updatedMovie.setFsk(movieData.getFsk());
        // version
        if (movieData.getVersion() != null) updatedMovie.setVersion(movieData.getVersion());
        // price
        if (movieData.getPrice() != null) updatedMovie.setPrice(movieData.getPrice());
        // director
        if (movieData.getDirector() != null) updatedMovie.setDirector(movieData.getDirector());
        // cast && cast not empty
        if (movieData.getCast() != null && checkArrayNoNullValues(movieData.getCast())) updatedMovie.setCast(movieData.getCast());
        // genres && cast not empty
        if (movieData.getGenres() != null && !movieData.getGenres().isEmpty()) updatedMovie.setGenres(movieData.getGenres());
        // trailerLink
        if (movieData.getTrailerLink() != null) updatedMovie.setTrailerLink(movieData.getTrailerLink());
        
        // checking date chronology 
        if (!checkDateChronology(updatedMovie)) {
            throw new IllegalStateException("Something wrong with chronology of dates of updatedMovie.");
        }

        return save(updatedMovie);
    }


    public Movie getById(Long id) {
        return movieRepository.findById(id).orElseThrow(() ->
            new NoSuchElementException("Could not find movie with id \"" + id + "\"."));
    }
    
    // TODO: should return iterable, because same title but different movie is possible
    public Movie getByTitle(String title) {
        return movieRepository.findByTitle(title).orElseThrow(() -> 
            new NoSuchElementException("Could not find movie with title \"" + title + "\"."));
    }
    
    
    public Movie getByTitleAndVersion(String title, MovieVersion version) {
        return movieRepository.findByTitleAndVersion(title, version).orElseThrow(() -> 
            new NoSuchElementException("Could not find movie with title \"" + title + "\" and version \"" + version + "\"."));
    }
    
    
    public List<Movie> getAll() {
        // order by release date, from latest to oldest
        return movieRepository.findAllByOrderByLocalReleaseDateDesc();
    }


    public Long getTotalWeeksInCinema(String title) {
        // getting movie by title from repo
        Movie movie = getByTitle(title);

        // calculating time between release and finish
        Long runtimeInMonths = movie.getLocalReleaseDate().until(movie.getLocalFinishingDate(), ChronoUnit.WEEKS);
        
        return runtimeInMonths;
    }
    

    public void delete(String title, MovieVersion version) {
        // find by title and version
        Movie movie = getByTitleAndVersion(title, version);
        
        movieRepository.delete(movie);
    }


    public boolean exists(String title, MovieVersion version) {
        return movieRepository.findByTitleAndVersion(title, version).isPresent();
    }
    
    
//// helper functions
    
    
    private Movie save(Movie movie) {
        return movieRepository.save(movie);
    }
    
    
    private boolean check(Movie movie) {
        // checking for null fields
        if (!checkNullValues(movie)) return false;
        
        // checking date chronology
        if (!checkDateChronology(movie)) return false;

        return true;
    }


    private boolean checkNullValues(Movie movie) {
        // title
        if (movie.getTitle() == null) return false;
        // duration
        if (movie.getDuration() == null) return false;
        // localReleaseDate
        if (movie.getLocalReleaseDate() == null) return false;
        // localFinishingDate
        if (movie.getLocalFinishingDate() == null) return false;
        // synopsis
        if (movie.getSynopsis() == null) return false;
        // fsk
        if (movie.getFsk() == null) return false;
        // version
        if (movie.getVersion() == null) return false;
        // price
        if (movie.getTitle() == null) return false;
        // director
        if (movie.getDirector() == null) return false;
        // cast && cast not empty
        if (movie.getCast() == null && !checkArrayNoNullValues(movie.getCast())) return false;
        // genres && genres not empty
        if (movie.getGenres() == null && movie.getGenres().isEmpty()) return false;
        // trailerLink
        if (movie.getTrailerLink() == null) return false;

        return true;
    }


    private boolean checkDateChronology(Movie movie) {
        return movie.getLocalReleaseDate().isBefore(movie.getLocalFinishingDate());
    }



    private <T> boolean checkArrayNoNullValues(T[] array) {
        for (T el : array) {
            if (el == null) return false;
        }

        return true;
    }
}