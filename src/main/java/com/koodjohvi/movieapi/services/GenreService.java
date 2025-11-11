package com.koodjohvi.movieapi.services;

import com.koodjohvi.movieapi.entities.Genre;
import com.koodjohvi.movieapi.entities.Movie;
import com.koodjohvi.movieapi.exception.DeletionNotAllowedException;
import com.koodjohvi.movieapi.exception.ResourceNotFoundException;
import com.koodjohvi.movieapi.repositories.GenreRepository;
import com.koodjohvi.movieapi.repositories.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    // create genre
    public Genre createGenre(Genre genre) {
        if (genreRepository.findByNameContainingIgnoreCase(genre.getName()).isPresent()) {
            throw new IllegalStateException("Genre with name '" + genre.getName() + "' already exists");
        }
        return genreRepository.save(genre);
    }

    // get all genres
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // get genre by ID
    @Transactional(readOnly = true)
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No genre found with ID: " + id));
    }

    // update genre(PATCH)
    public Genre updateGenre(Long id, Genre updatedGenre) {
        Genre existing = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));

        // Ensure ID is not modified
        if (updatedGenre.getId() != null && !updatedGenre.getId().equals(id)) {
            throw new IllegalArgumentException("Cannot modify genre ID");
        }

        // Update only the name field
        if (updatedGenre.getName() != null && !updatedGenre.getName().trim().isEmpty()) {
            // Check for duplicate name (excluding current genre)
            if (!existing.getName().equalsIgnoreCase(updatedGenre.getName()) &&
                    genreRepository.findByNameContainingIgnoreCase(updatedGenre.getName()).isPresent()) {
                throw new IllegalStateException("Genre name '" + updatedGenre.getName() + "' is already taken");
            }
            existing.setName(updatedGenre.getName().trim());
        }

        return genreRepository.save(existing);
    }

    // delete genre either by force or not
    @Transactional
    public void deleteGenre(Long id, boolean force) {
        Genre genre = genreRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + id));

        // if force is off and genre has associated movies
        if(!force && !genre.getMovies().isEmpty()) {
            throw new DeletionNotAllowedException(
                "Cannot delete genre '" + genre.getName() +
                "' because genre has associated movie(s)."
            );
        }

        // if force is on
        if(force) {
            // Clear all relationships in a single batch operation
            movieRepository.clearGenreRelationships(genre.getId());
        }

        genreRepository.delete(genre);
    }
}
