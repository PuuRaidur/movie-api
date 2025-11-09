package com.koodjohvi.movieapi.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    @Column(nullable = false)
    private String title;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year cannot be before 1888 (first movie year)")
    @Max(value = 2100, message = "Release year cannot be in the future")
    private Integer releaseYear;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    @Max(value = 600, message = "Duration cannot exceed 600 minutes (10 hours)")
    private Integer duration;

    @ManyToMany
    @NotEmpty(message = "Movie must have at least one genre")
    @JoinTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "movie_actors",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors =  new HashSet<>();

    public Movie() {}
    public Movie(String title, Integer releaseYear, Integer duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Actor> getActors() {
        return this.actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(this.id, movie.id)
                && Objects.equals(this.title, movie.title)
                && Objects.equals(this.releaseYear, movie.releaseYear)
                && Objects.equals(this.duration, movie.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "Movie{" + "id=" + this.id + ", name='" + this.title + "'" + ", releaseYear=" + this.releaseYear + ", duration=" + this.duration + '}';
    }
}
