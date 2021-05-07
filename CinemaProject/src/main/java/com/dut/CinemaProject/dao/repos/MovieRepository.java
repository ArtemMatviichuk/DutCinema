package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.dao.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findMovieByIsBlocked(Boolean isBlocked);
    @Query(value="SELECT * FROM movies m WHERE m.id = (select movie_id from movie_genres mg inner join :genres on genres.id = mg.genre_id)", nativeQuery = true)
    List<Movie> findMoviesByGenres(@Param("genres")List<Genre> genres);
}
