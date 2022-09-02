package com.example.myCinema.movie;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myCinema.CheckEntity;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class MovieService extends CheckEntity {

    private final MovieRepository movieRepository;


    @Transactional
    public Movie addNew(Movie movie) {

        // checking movie data
        movieValid(movie);
        
        // checking if movie already exists
        if (exists(movie.getTitle(), movie.getVersion())) 
            throw new IllegalStateException("Movie with title \"" + movie.getTitle() + "\" in version \"" + movie.getVersion() + "\" does already exist.");
        
        return save(movie);
    }
    

    public Movie update(Movie movieContainer) {
        
        // checking if id is null
        if (movieContainer.getId() == null) 
            throw new IllegalStateException("Id of movieContainer must not be null.");
        
        
        // getting movie with given id from repo
        Movie movieToUpdate = getById(movieContainer.getId());
        
        // title
        if (!objectNullOrEmpty(movieContainer.getTitle())) movieToUpdate.setTitle(movieContainer.getTitle());
        // duration
        if (!objectNullOrEmpty(movieContainer.getDuration())) movieToUpdate.setDuration(movieContainer.getDuration());
        // localReleaseDate
        if (!objectNullOrEmpty(movieContainer.getLocalReleaseDate())) movieToUpdate.setLocalReleaseDate(movieContainer.getLocalReleaseDate());
        // localFinishingDate
        if (!objectNullOrEmpty(movieContainer.getLocalFinishingDate())) movieToUpdate.setLocalFinishingDate(movieContainer.getLocalFinishingDate());
        // synopsis
        if (!objectNullOrEmpty(movieContainer.getSynopsis())) movieToUpdate.setSynopsis(movieContainer.getSynopsis());
        // fsk
        if (!objectNullOrEmpty(movieContainer.getFsk())) movieToUpdate.setFsk(movieContainer.getFsk());
        // version
        if (!objectNullOrEmpty(movieContainer.getVersion())) movieToUpdate.setVersion(movieContainer.getVersion());
        // price
        if (!objectNullOrEmpty(movieContainer.getPrice())) movieToUpdate.setPrice(movieContainer.getPrice());
        // director
        if (!objectNullOrEmpty(movieContainer.getDirector())) movieToUpdate.setDirector(movieContainer.getDirector());
        // cast
        updateCast(movieContainer, movieToUpdate);
        // genres
        if (!iterableNullOrEmpty(movieContainer.getGenres())) movieToUpdate.setGenres(movieContainer.getGenres());
        // trailerLink
        if (!objectNullOrEmpty(movieContainer.getTrailerLink())) movieToUpdate.setTrailerLink(movieContainer.getTrailerLink());
        
        // checking date chronology 
        checkReleaseAndFinishingDates(movieToUpdate);

        return save(movieToUpdate);
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


    public void deleteAll() {

        movieRepository.deleteAll();
    }


    public boolean exists(String title, MovieVersion version) {

        return movieRepository.findByTitleAndVersion(title, version).isPresent();
    }

//// helper functions
    
    
    private Movie save(Movie movie) {

        return movieRepository.save(movie);
    }
    
    
    private boolean movieValid(Movie movie) {

        return 
            // null or empty strings
            !hasNullValue(movie) &&

            // dates in order
            checkReleaseAndFinishingDates(movie);
    }


    /**
     * Checks for illegal values: 
     * <p>null
     * <p>is an empty string ("")
     * <p>iterable empty
     * <p>iterable contains null
     * <p>iterable contains emtpy string ("")
     * 
     * @param movie the movie to check.
     * @return true if at least one of the movies fields has an illegal value.
     */
    private boolean hasNullValue(Movie movie) {

        if (
            // title
            objectNullOrEmpty(movie.getTitle()) ||
            // duration
            objectNullOrEmpty(movie.getDuration()) ||
            // localReleaseDate
            objectNullOrEmpty(movie.getLocalReleaseDate()) ||
            // localFinishingDate
            objectNullOrEmpty(movie.getLocalFinishingDate()) ||
            // synopsis
            objectNullOrEmpty(movie.getSynopsis()) ||
            // fsk
            objectNullOrEmpty(movie.getFsk()) ||
            // version
            objectNullOrEmpty(movie.getVersion()) ||
            // price
            objectNullOrEmpty(movie.getPrice()) ||
            // director
            objectNullOrEmpty(movie.getDirector()) ||
            // cast
            iterableNullOrEmpty(movie.getCast()) ||
            // genres
            iterableNullOrEmpty(movie.getGenres()) ||
            // trailerLink
            objectNullOrEmpty(movie.getTrailerLink()))
            
                throw new IllegalStateException("Movie data contains null values or empty strings ('').");
            
        return false;
    }


    private boolean checkReleaseAndFinishingDates(Movie movie) {
        
        if (movie.getLocalReleaseDate().isAfter(movie.getLocalFinishingDate())) 
            throw new IllegalStateException("Local release date cannot be after local finishing date.");

        return true;
    }


    /**
     * Updates cast lists. Iterates both lists and replaces new notNull values
     * at the exact same index. If list with new data is longer than the existing
     * one, the overhead is simply added to the existing list.
     * 
     * <p>Example:
     * 
     * <p>ExistingList = {1, 2, 3}; ListWithNewData = {1, null, 4, 5, 6};
     * <p>newList = {1, 2, 4, 5, 6};
     * @param movieContainer
     * @param movieToUpdate
     */
    private void updateCast(Movie movieContainer, Movie movieToUpdate) {

        // getting cast lists
        List<String> castToUpdate = movieToUpdate.getCast();
        List<String> castContainer = movieContainer.getCast();

        for (int i = 0; i < castContainer.size(); i++) {
            // if there's a new value in the container
            if (!objectNullOrEmpty(castContainer.get(i))) {
                try {
                    // replace member in movieToUpdate with member in movieContainer
                    castToUpdate.set(i, movieContainer.getCast().get(i));

                } catch (IndexOutOfBoundsException e) {
                    // case castToUpdate is smaller than castContainer
                    castToUpdate.add(movieContainer.getCast().get(i));
                }
            }
        }
    }
}