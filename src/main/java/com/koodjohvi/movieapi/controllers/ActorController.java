package com.koodjohvi.movieapi.controllers;

import com.koodjohvi.movieapi.entities.Actor;
import com.koodjohvi.movieapi.services.ActorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // get actor(s) by name (GET /api/actors)
    // supports ?name= and pagination(ex: ?page=0?size=10)
    @GetMapping
    public Page<Actor> getActors(@RequestParam(required = false) String name, Pageable pageable) {
        if (name != null) {
            return actorService.getActorsByNameContainingIgnoreCase(name, pageable);
        }
        return actorService.getAllActors(pageable);
    }

    // get actor by ID (GET /api/actors/{ID})
    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
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
