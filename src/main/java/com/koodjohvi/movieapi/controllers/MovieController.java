package com.koodjohvi.movieapi.controllers;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.entities.Movie;
import com.koodjohvi.movieapi.exception.ResourceNotFoundException;
import com.koodjohvi.movieapi.repositories.ActorRepository;
import com.koodjohvi.movieapi.repositories.GenreRepository;
import com.koodjohvi.movieapi.services.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {
    private final MovieService movieService;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    public MovieController(MovieService movieService,
                           GenreRepository genreRepository,
                           ActorRepository actorRepository) {
        this.movieService = movieService;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    // create movie (POST /api/movies)
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie saved = movieService.createMovie(movie);
        return ResponseEntity.status(201).body(saved);
    }

    // get movies by filter(genre, year, actor, title) (GET /api/movies)
    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long actor,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        try {
            // Check if pagination is NOT requested (i.e., user didn't provide page/size)
            boolean isUnpaginated = !isPaginationRequested();

            if (genre != null) {
                // Validates genre exists first
                if (!genreRepository.existsById(genre)) {
                    throw new ResourceNotFoundException("Genre not found with id: " + genre);
                }
                return ResponseEntity.ok(movieService.getMoviesByGenre(genre, pageable, isUnpaginated));
            } else if (year != null) {
                return ResponseEntity.ok(movieService.getMoviesByYear(year, pageable, isUnpaginated));
            } else if (actor != null) {
                // Validates actor exists first
                if (!actorRepository.existsById(actor)) {
                    throw new ResourceNotFoundException("Actor not found with id: " + actor);
                }
                return ResponseEntity.ok(movieService.getMoviesByActor(actor, pageable, isUnpaginated));
            } else if (title != null) {
                return ResponseEntity.ok(movieService.getMoviesByTitleContainingIgnoreCase(title, pageable, isUnpaginated));
            } else {
                return ResponseEntity.ok(movieService.getAllMovies(pageable, isUnpaginated));
            }
        } catch (Exception e) {
            if (e.getMessage() != null &&
                    (e.getMessage().contains("Invalid page") || e.getMessage().contains("Invalid size"))) {
                return ResponseEntity.badRequest().body("Invalid pagination parameters");
            }
            throw e;
        }
    }

    // Helper: detect if user explicitly asked for pagination
    private boolean isPaginationRequested() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getParameter("page") != null || request.getParameter("size") != null;
    }

    // search for movies (GET /api/movies/search?title=)
    @GetMapping("/search")
    public Object searchMovies(@RequestParam String title, Pageable pageable) {
        boolean isUnpaginated = !isPaginationRequested();
        return movieService.getMoviesByTitleContainingIgnoreCase(title, pageable, isUnpaginated);
    }

    // get movie by ID (GET /api/movies/{id})
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // get actors in movie by ID(GET /api/movies/{id}/actors)
    @GetMapping("/{movieId}/actors")
    public ResponseEntity<List<Actor>> getActorsByMovie(@PathVariable Long movieId) {
        List<Actor> actors = movieService.getActorsByMovie(movieId);
        return ResponseEntity.ok(actors);
    }

    // update movie by ID(PATCH /api/movies/{id})
    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        Movie updated = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updated); // 200 OK
    }

    // delete movie either by force or not (DELETE /api/movies/{id}?force={true/false})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        movieService.deleteMovie(id, force);
        return ResponseEntity.noContent().build();
    }
}
