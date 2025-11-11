package com.koodjohvi.movieapi.repositories;

import com.koodjohvi.movieapi.entities.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // custom query methods as mandatory
    List<Movie> findByGenresId(Long genreId);
    @Query("SELECT m FROM Movie m WHERE m.releaseYear = :year")
    List<Movie> findByReleaseYear(Integer year);
    List<Movie> findByActorsId(Long actorId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Page<Movie> findByGenresId(Long genreId, Pageable pageable);
    @Query("SELECT m FROM Movie m WHERE m.releaseYear = :year")
    Page<Movie> findByReleaseYear(Integer year, Pageable pageable);
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // clear all movies for a specific actor (when force-deleting actor)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM movie_actors WHERE actor_id = :actorId", nativeQuery = true)
    void clearActorRelationships(@Param("actorId") Long actorId);

    // clear all movies for a specific genre (when force-deleting genre)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM movie_genres WHERE genre_id = :genreId", nativeQuery = true)
    void clearGenreRelationships(@Param("genreId") Long genreId);

    // clear all actors for a specific movie (when force-deleting movie)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM movie_actors WHERE movie_id = :movieId", nativeQuery = true)
    void clearMovieActorRelationships(@Param("movieId") Long movieId);

    // clear all genres for a specific movie (when force-deleting movie)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM movie_genres WHERE movie_id = :movieId", nativeQuery = true)
    void clearMovieGenreRelationships(@Param("movieId") Long movieId);
}
