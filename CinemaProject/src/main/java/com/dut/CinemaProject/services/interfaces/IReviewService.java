package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dto.Review.NewReview;
import com.dut.CinemaProject.dto.Review.ReviewDto;

import java.util.List;

public interface IReviewService {
    ReviewDto createReview(NewReview newReview);
    ReviewDto getById(Long id);
    List<ReviewDto> getReviewsByMovie(Long movieId);
    void deleteReview(Long id);
    ReviewDto updateReview(Long id, String newText);
    List<ReviewDto> getAll();
}
