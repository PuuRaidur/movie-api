package com.koodjohvi.movieapi.services;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.entities.Genre;
import com.koodjohvi.movieapi.entities.Movie;
import com.koodjohvi.movieapi.exception.ResourceNotFoundException;
import com.koodjohvi.movieapi.repositories.ActorRepository;
import com.koodjohvi.movieapi.repositories.GenreRepository;
import com.koodjohvi.movieapi.repositories.MovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final GenreRepository genreRepository;

    public MovieService(MovieRepository movieRepository,  ActorRepository actorRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.genreRepository = genreRepository;
    }

    // create movie
    public Movie createMovie(Movie movie) {
        // Validate required fields
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title is required");
        }
        if (movie.getReleaseYear() == null) {
            throw new IllegalArgumentException("No release year provided");
        }
        if (movie.getDuration() == null || movie.getDuration() <= 0) {
            throw new IllegalArgumentException("Invalid duration");
        }

        // Validate and fetch genres
        if (movie.getGenres() != null) {
            Set<Genre> validatedGenres = new HashSet<>();
            for (Genre genre : movie.getGenres()) {
                if (genre.getId() == null) {
                    throw new IllegalArgumentException("Genre ID is required");
                }
                validatedGenres.add(genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + genre.getId())));
            }
            movie.setGenres(validatedGenres);
        }

        // Validate and fetch actors
        if (movie.getActors() != null) {
            Set<Actor> validatedActors = new HashSet<>();
            for (Actor actor : movie.getActors()) {
                if (actor.getId() == null) {
                    throw new IllegalArgumentException("Actor ID is required");
                }
                validatedActors.add(actorRepository.findById(actor.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found: " + actor.getId())));
            }
            movie.setActors(validatedActors);
        }

        return movieRepository.save(movie);
    }

    // get all movies with pagination
    @Transactional(readOnly = true)
    public Object getAllMovies(Pageable pageable, boolean unpaginated) {
        return unpaginated ? movieRepository.findAll() : movieRepository.findAll(pageable);
    }

    // get movie by ID
    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No movie found with ID: " + id));
    }

    // get movies by year with pagination
    @Transactional(readOnly = true)
    public Object getMoviesByYear(Integer year, Pageable pageable, boolean unpaginated) {
        return unpaginated
        ? movieRepository.findByReleaseYear(year)
        : movieRepository.findByReleaseYear(year, pageable);
    }

    // get movies by genre with pagination
    @Transactional(readOnly = true)
    public Object getMoviesByGenre(Long genreId, Pageable pageable, boolean unpaginated) {
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException("Genre not found: " + genreId);
        }

        return unpaginated
        ? movieRepository.findByGenresId(genreId)
        : movieRepository.findByGenresId(genreId, pageable);
    }

    // get movies by actor with pagination
    @Transactional(readOnly = true)
    public Object getMoviesByActor(Long actorId, Pageable pageable, boolean unpaginated) {
        if (!actorRepository.existsById(actorId)) {
            throw new ResourceNotFoundException("Actor not found: " + actorId);
        }

        return unpaginated
        ? movieRepository.findByActorsId(actorId)
        : movieRepository.findByActorsId(actorId, pageable);
    }

    // get movies by title with pagination
    public Object getMoviesByTitleContainingIgnoreCase(String title, Pageable pageable, boolean unpaginated) {
        return unpaginated
        ? movieRepository.findByTitleContainingIgnoreCase(title)
        : movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    // get all actors in a movie
    @Transactional(readOnly = true)
    public List<Actor> getActorsByMovie(Long movieId) {
        // validate movie exists first
        Movie movie = movieRepository.findById(movieId)
        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));
        return new ArrayList<>(movie.getActors());
    }

    // update movie(PATCH)
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        // Ensure ID is not modified
        if (updatedMovie.getId() != null && !updatedMovie.getId().equals(id)) {
            throw new IllegalArgumentException("Cannot modify movie ID");
        }

        // Update title if provided
        if (updatedMovie.getTitle() != null && !updatedMovie.getTitle().trim().isEmpty()) {
            existing.setTitle(updatedMovie.getTitle().trim());
        }

        // Update release year if provided
        if (updatedMovie.getReleaseYear() != null) {
            if (updatedMovie.getReleaseYear() < 1888 || updatedMovie.getReleaseYear() > LocalDate.now().getYear()) {
                throw new IllegalArgumentException("Invalid release year");
            }
            existing.setReleaseYear(updatedMovie.getReleaseYear());
        }

        // Update duration if provided
        if (updatedMovie.getDuration() != null) {
            if (updatedMovie.getDuration() <= 0) {
                throw new IllegalArgumentException("Invalid duration");
            }
            existing.setDuration(updatedMovie.getDuration());
        }

        // Update genres if provided
        if (updatedMovie.getGenres() != null) {
            Set<Genre> validatedGenres = new HashSet<>();
            for (Genre genre : updatedMovie.getGenres()) {
                if (genre.getId() == null) {
                    throw new IllegalArgumentException("Genre ID is required");
                }
                validatedGenres.add(genreRepository.findById(genre.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found: " + genre.getId())));
            }
            existing.setGenres(validatedGenres);
        }

        // Update actors if provided
        if (updatedMovie.getActors() != null) {
            Set<Actor> validatedActors = new HashSet<>();
            for (Actor actor : updatedMovie.getActors()) {
                if (actor.getId() == null) {
                    throw new IllegalArgumentException("Actor ID is required");
                }
                validatedActors.add(actorRepository.findById(actor.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor not found: " + actor.getId())));
            }
            existing.setActors(validatedActors);
        }

        return movieRepository.save(existing);
    }

    // delete movie
    public void deleteMovie(Long id) {
        if(!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("No movie found with ID: " + id);
        }
        movieRepository.deleteById(id);
    }
}