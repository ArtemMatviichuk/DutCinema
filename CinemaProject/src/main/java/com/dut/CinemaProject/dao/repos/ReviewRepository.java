package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.Movie;
import com.dut.CinemaProject.dao.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByMovie(Movie movie);
}
