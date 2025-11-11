package com.koodjohvi.movieapi.repositories;

import com.koodjohvi.movieapi.entities.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    // custom query methods as mandatory
    List<Actor> findByNameContainingIgnoreCase(String name);
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
}
