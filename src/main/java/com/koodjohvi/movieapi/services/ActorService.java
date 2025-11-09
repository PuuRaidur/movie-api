package com.koodjohvi.movieapi.services;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.entities.Movie;
import com.koodjohvi.movieapi.exception.DeletionNotAllowedException;
import com.koodjohvi.movieapi.exception.ResourceNotFoundException;
import com.koodjohvi.movieapi.repositories.ActorRepository;
import com.koodjohvi.movieapi.repositories.MovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Service
@Transactional
public class ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    // create actor
    public Actor createActor(Actor actor) {
        // Validate birthdate is in the past
        if (actor.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date is required");
        }
        if (actor.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }

        return actorRepository.save(actor);
    }

    // get all actors with pagination
    @Transactional(readOnly = true)
    public Object getAllActors(Pageable pageable, boolean unpaginated) {
        return unpaginated
        ? actorRepository.findAll()
        : actorRepository.findAll(pageable);
    }

    // get actor by ID
    @Transactional(readOnly = true)
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No actor found with ID: " + id));
    }

    // get actors by name with pagination
    @Transactional(readOnly = true)
    public Object getActorsByNameContainingIgnoreCase(String name, Pageable pageable, boolean unpaginated) {
        return unpaginated
        ? actorRepository.findByNameContainingIgnoreCase(name)
        : actorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // update actor(PATCH)
    public Actor updateActor(Long id, Actor updatedActor) {
        Actor existing = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));

        // Ensure ID is not modified
        if (updatedActor.getId() != null && !updatedActor.getId().equals(id)) {
            throw new IllegalArgumentException("Cannot modify actor ID");
        }

        // Update name if provided
        if (updatedActor.getName() != null && !updatedActor.getName().trim().isEmpty()) {
            existing.setName(updatedActor.getName().trim());
        }

        // Update birthdate if provided
        if (updatedActor.getBirthDate() != null) {
            if (updatedActor.getBirthDate().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Birth date cannot be in the future");
            }
            existing.setBirthDate(updatedActor.getBirthDate());
        }

        // Update movies if provided
        if (updatedActor.getMovies() != null) {
            Set<Movie> validatedMovies = new HashSet<>();
            for (Movie movie : updatedActor.getMovies()) {
                if (movie.getId() == null) {
                    throw new IllegalArgumentException("Movie ID is required");
                }
                validatedMovies.add(movieRepository.findById(movie.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found: " + movie.getId())));
            }
            existing.setMovies(validatedMovies);
        }

        return actorRepository.save(existing);
    }

    // delete actor either by force or not
    public void deleteActor(Long id, boolean force) {
        Actor actor = actorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No actor found with ID: " + id));

        // if force is off and actor has associated movies
        if(!force && !actor.getMovies().isEmpty()) {
            throw new DeletionNotAllowedException(
                "Cannot delete actor '" + actor.getName() +
                "' because actor has associated movie(s)"
            );
        }

        // if force is on
        if (force) {
            // remove actor from all associated movies
            for(Movie movie : actor.getMovies()) {
                movie.getActors().remove(actor);
                movieRepository.save(movie);
            }
        }

        actorRepository.deleteById(id);
    }
}
