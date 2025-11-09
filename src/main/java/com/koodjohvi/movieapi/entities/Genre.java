package com.koodjohvi.movieapi.entities;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Genre name is required")
    @Size(min = 2, max = 30, message = "Genre name must be between 2 and 30 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public Genre() {}
    public Genre(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Genre)) return false;
        Genre genre = (Genre) o;
        return Objects.equals(this.id, genre.id)
                && Objects.equals(this.name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "Genre{" + "id=" + this.id + ", name='" + this.name + '}';
    }
}
