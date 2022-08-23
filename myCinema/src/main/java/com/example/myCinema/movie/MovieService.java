package com.example.myCinema.movie;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    interface Check {
        Boolean check(Object obj);
    }


    @Transactional
    public Movie addNew(Movie movie) {
        // checking movie data
        if (!check(movie) || exists(movie.getTitle(), movie.getVersion())) {
            throw new IllegalStateException("Something wrong with movie input data.");
        }
        
        return save(movie);
    }
    

    // TODO: check collections seperatly
    public Movie update(Movie movieData) {
        // checking if id is null
        if (movieData.getId() == null) {
            throw new IllegalStateException("Id of movieData must not be null.");
        }
        
        // getting movie with given id from repo
        Movie updatedMovie = getById(movieData.getId());
        
        // updating not null values from movieData
        // title
        if (movieData.getTitle() != null && !movieData.getTitle().equals("")) updatedMovie.setTitle(movieData.getTitle());
        // duration
        if (movieData.getDuration() != null) updatedMovie.setDuration(movieData.getDuration());
        // localReleaseDate
        if (movieData.getLocalReleaseDate() != null) updatedMovie.setLocalReleaseDate(movieData.getLocalReleaseDate());
        // localFinishingDate
        if (movieData.getLocalFinishingDate() != null) updatedMovie.setLocalFinishingDate(movieData.getLocalFinishingDate());
        // synopsis
        if (movieData.getSynopsis() != null && !movieData.getSynopsis().equals("")) updatedMovie.setSynopsis(movieData.getSynopsis());
        // fsk
        if (movieData.getFsk() != null) updatedMovie.setFsk(movieData.getFsk());
        // version
        if (movieData.getVersion() != null) updatedMovie.setVersion(movieData.getVersion());
        // price
        if (movieData.getPrice() != null) updatedMovie.setPrice(movieData.getPrice());
        // director
        if (movieData.getDirector() != null && !movieData.getDirector().equals("")) updatedMovie.setDirector(movieData.getDirector());
        // cast
        if (movieData.getCast() != null) updatedMovie.setCast(movieData.getCast());
        // genres && cast not empty
        if (movieData.getGenres() != null && !movieData.getGenres().isEmpty()) updatedMovie.setGenres(movieData.getGenres());
        // trailerLink
        if (movieData.getTrailerLink() != null && !movieData.getTrailerLink().equals("")) updatedMovie.setTrailerLink(movieData.getTrailerLink());
        
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
    
    
    public List<Movie> getByTitle(String title) {
        List<Movie> list = movieRepository.findByTitle(title);

        if (list.isEmpty()) throw new NoSuchElementException("Could not find movies with title \"" + title + "\".");

        return list;
    }
    
    
    public Movie getByTitleAndVersion(String title, MovieVersion version) {
        return movieRepository.findByTitleAndVersion(title, version).orElseThrow(() -> 
            new NoSuchElementException("Could not find movie with title \"" + title + "\" and version \"" + version + "\"."));
    }
    
    
    public List<Movie> getAll() {
        // order by release date, from latest to oldest
        return movieRepository.findAllByOrderByLocalReleaseDateDesc();
    }


    public Long getTotalWeeksInCinema(String title, MovieVersion version) {
        // getting movie by title from repo
        Movie movie = getByTitleAndVersion(title, version);

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


    private <T> boolean checkCollection(Collection<T> collection) {
        // TODO: implement
        return true;
    }

    private boolean checkNullValues(Movie movie) {
        // title
        if (movie.getTitle() == null || movie.getTitle().equals("")) return false;
        // duration
        if (movie.getDuration() == null) return false;
        // localReleaseDate
        if (movie.getLocalReleaseDate() == null) return false;
        // localFinishingDate
        if (movie.getLocalFinishingDate() == null) return false;
        // synopsis
        if (movie.getSynopsis() == null || movie.getSynopsis().equals("")) return false;
        // fsk
        if (movie.getFsk() == null) return false;
        // version
        if (movie.getVersion() == null) return false;
        // price
        if (movie.getPrice() == null) return false;
        // director
        if (movie.getDirector() == null || movie.getDirector().equals("")) return false;
        // cast
        if (movie.getCast() == null || movie.getCast().contains(null) || movie.getCast().contains("")) return false;
        // genres
        if (movie.getGenres() == null || movie.getGenres().contains(null)) return false;
        // trailerLink
        if (movie.getTrailerLink() == null || movie.getTrailerLink().equals("")) return false;

        return true;
    }


    private boolean checkDateChronology(Movie movie) {
        return movie.getLocalReleaseDate().isBefore(movie.getLocalFinishingDate());
    }
}