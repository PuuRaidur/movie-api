package com.koodjohvi.movieapi.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(nullable = false)
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @ManyToMany
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
