package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = "SELECT * FROM (SELECT * FROM sessions WHERE date > NOW() AND is_canceled = 0 ORDER BY date) ses GROUP BY ses.movie_id",
            nativeQuery = true)
    List<Session> getActualSessions();
    @Query(value="SELECT * FROM Sessions  WHERE date > NOW() AND is_canceled = 0 AND hall_id = :hallId", nativeQuery = true)
    List<Session> getActualSessionsByHallId(@Param("hallId") Long hallId);
    @Query(value="SELECT * FROM Sessions WHERE date > NOW() AND is_canceled = 0 AND movie_id = :movieId", nativeQuery = true)
    List<Session> getActualSessionsByMovieId(@Param("movieId") Long movieId);
}
