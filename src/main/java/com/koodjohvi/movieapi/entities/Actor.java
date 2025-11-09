package com.koodjohvi.movieapi.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

@Entity
@Table(name = "actor")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Actor name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @Column(nullable = false)
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "actors")
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    public Actor() {}
    public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
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

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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
        if(!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return Objects.equals(this.id, actor.id)
                && Objects.equals(this.name, actor.name)
                && Objects.equals(this.birthDate, actor.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "Actor{" + "id=" + this.id + ", name='" + this.name + "'" + ", birthDate=" + this.birthDate + '}';
    }
}
