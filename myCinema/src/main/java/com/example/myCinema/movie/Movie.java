package com.example.myCinema.movie;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "_movie_id_sequence")
    @SequenceGenerator(name = "_movie_id_sequence", allocationSize = 1) 
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    private String title;

    /**in minutes */
    @Column(nullable = false)
    private Integer duration; 

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate localReleaseDate;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate localFinishingDate;

    @Column(nullable = false)
    private String synopsis;   

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FSK fsk;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovieVersion version;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false) 
    private String director;

    @ElementCollection(fetch = FetchType.LAZY)
    @Column(name = "castMember")
    @EqualsAndHashCode.Exclude
    private Set<String> cast;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Column(name = "genres")
    @EqualsAndHashCode.Exclude
    private Set<Genre> genres; 

    @Column(nullable = false)
    @EqualsAndHashCode.Exclude
    private String trailerLink;


    public Movie(String title, 
                 Integer duration, 
                 LocalDate localReleaseDate, 
                 LocalDate localFinishingDate,
                 String synopsis, 
                 FSK fsk,
                 MovieVersion version, 
                 Double price, 
                 String director,
                 Set<String> cast,
                 Set<Genre> genres,
                 String trailerLink) {
        this.title = title;
        this.duration = duration;
        this.localReleaseDate = localReleaseDate;
        this.localFinishingDate = localFinishingDate;
        this.synopsis = synopsis;
        this.fsk = fsk;
        this.version = version;
        this.price = price;
        this.director = director;
        this.cast = cast;
        this.genres = genres;
        this.trailerLink = trailerLink;
    }


    @Override
    public String toString() {
        return this.title + " " + this.version.toString();
    }
}


@Getter
@Setter
class MovieWrapper {
    private FSK[] fsk = FSK.values();

    private MovieVersion[] version = MovieVersion.values();

    private Genre[] genres = Genre.values();

    private boolean[] toggledGenres = new boolean[genres.length];

    private String[] movieCast = {"", "", ""};
}