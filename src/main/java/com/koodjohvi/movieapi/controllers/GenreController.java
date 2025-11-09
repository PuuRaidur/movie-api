package com.koodjohvi.movieapi.controllers;

import com.koodjohvi.movieapi.entities.Genre;
import com.koodjohvi.movieapi.services.GenreService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@Validated
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // create genres (POST /api/genres)
    @PostMapping
    public ResponseEntity<Genre> createGenre(@Valid @RequestBody Genre genre) {
        Genre saved = genreService.createGenre(genre);
        return ResponseEntity.status(201).body(saved);
    }

    // get all genres (GET /api/genres)
    // supports ?genre= and pagination (ex: ?page0?size=10)
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // get genre by ID (GET /api/genres/{id})
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    // update genre (PATCH /api/genres/{id})
    @PatchMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable Long id, @Valid @RequestBody Genre genre) {
        Genre updated = genreService.updateGenre(id, genre);
        return ResponseEntity.ok(updated);
    }

    // delete genre either by force or not (DELETE /api/genres/{id}?froce={true/false})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        genreService.deleteGenre(id, force);
        return ResponseEntity.noContent().build();
    }
}
