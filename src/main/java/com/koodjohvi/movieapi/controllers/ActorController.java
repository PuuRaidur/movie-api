package com.koodjohvi.movieapi.controllers;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.services.ActorService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/actors")
@Validated
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // create actor (POST /api/actors)
    @PostMapping
    public ResponseEntity<Actor> addActor(@Valid @RequestBody Actor actor) {
        Actor saved = actorService.createActor(actor);
        return ResponseEntity.status(201).body(saved);
    }

    // get actor(s) by name or with pagination(GET /api/actors?name or GET /api/actors?page=0&size=10)
    @GetMapping
    public ResponseEntity<?> getAllActors(@RequestParam(required = false) String name, Pageable pageable) {
        try {
            boolean isUnpaginated = !isPaginationRequested();

            if (name != null) {
                return ResponseEntity.ok(actorService.getActorsByNameContainingIgnoreCase(name, pageable, isUnpaginated));
            } else {
                return ResponseEntity.ok(actorService.getAllActors(pageable, isUnpaginated));
            }
        } catch (Exception e) {
            if (e.getMessage() != null &&
                    (e.getMessage().contains("Invalid page") || e.getMessage().contains("Invalid size"))) {
                return ResponseEntity.badRequest().body("Invalid pagination parameters");
            }
            throw e;
        }
    }

    // Helper: detect if user provided page or size
    private boolean isPaginationRequested() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getParameter("page") != null || request.getParameter("size") != null;
    }

    // get actor by ID (GET /api/actors/{ID})
    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    @GetMapping("/search")
    public Object searchActors(@RequestParam String name, Pageable pageable) {
        boolean isUnpaginated = !isPaginationRequested();
        return actorService.getActorsByNameContainingIgnoreCase(name, pageable, isUnpaginated);
    }

    // update actor by ID (PATCH /api/actors/{id})
    @PatchMapping("/{id}")
    public ResponseEntity<Actor> updateActor(@PathVariable Long id, @Valid @RequestBody Actor actor) {
        Actor updated = actorService.updateActor(id, actor);
        return ResponseEntity.ok(updated);
    }

    // delete actor by ID (DELETE /api/actors/{id}?force={true/false})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean force) {
        actorService.deleteActor(id, force);
        return ResponseEntity.noContent().build();
    }
}
