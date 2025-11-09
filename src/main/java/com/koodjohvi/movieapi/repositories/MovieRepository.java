package com.koodjohvi.movieapi.repositories;

import com.koodjohvi.movieapi.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // custom query methods as mandatory
    Page<Movie> findByGenresId(Long genreId, Pageable pageable);
    Page<Movie> findByReleaseYear(Integer year, Pageable pageable);
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
