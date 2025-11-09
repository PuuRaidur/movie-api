package com.koodjohvi.movieapi.controllers;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.entities.Movie;
import com.koodjohvi.movieapi.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Validated
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // create movie (POST /api/movies)
    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie saved = movieService.createMovie(movie);
        return ResponseEntity.status(201).body(saved);
    }

    // get movies by filter(genre, year, actor, title) (GET /api/movies)
    @GetMapping
    public ResponseEntity<?> getMovies(
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long actor,
            @RequestParam(required = false) String title,
            Pageable pageable
    ) {
        try {
            if (genre != null) {
                return ResponseEntity.ok(movieService.getMoviesByGenre(genre, pageable));
            } else if (year != null) {
                return ResponseEntity.ok(movieService.getMoviesByYear(year, pageable));
            } else if (actor != null) {
                return ResponseEntity.ok(movieService.getMoviesByActor(actor, pageable));
            } else if (title != null) {
                return ResponseEntity.ok(movieService.getMoviesByTitleContainingIgnoreCase(title, pageable));
            } else {
                return ResponseEntity.ok(movieService.getAllMovies(pageable));
            }
        } catch (Exception e) {
            // Handle invalid pagination parameters
            if (e.getMessage().contains("Invalid page") || e.getMessage().contains("Invalid size")) {
                return ResponseEntity.badRequest().body("Invalid pagination parameters");
            }
            throw e;
        }
    }

    // search for movies (GET /api/movies/search?title=)
    @GetMapping("/search")
    public Page<Movie> searchMovies(@RequestParam String title, Pageable pageable) {
        return movieService.getMoviesByTitleContainingIgnoreCase(title, pageable);
    }

    // get movie by ID (GET /api/movies/{id})
    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    // get actors in movie by ID(GET /api/movies/{id}/actors)
    @GetMapping("/{id}/actors")
    public List<Actor> getActorsByMovie(@PathVariable Long id) {
        return movieService.getActorsByMovie(id);
    }

    // update movie by ID(PATCH /api/movies/{id})
    @PatchMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movie) {
        Movie updated = movieService.updateMovie(id, movie);
        return ResponseEntity.ok(updated); // 200 OK
    }

    // delete movie either by force or not (DELETE /api/movies/{id}?force={true/false})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
