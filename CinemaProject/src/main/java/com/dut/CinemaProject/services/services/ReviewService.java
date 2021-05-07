package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.domain.Movie;
import com.dut.CinemaProject.dao.domain.Review;
import com.dut.CinemaProject.dao.domain.User;
import com.dut.CinemaProject.dao.repos.MovieRepository;
import com.dut.CinemaProject.dao.repos.ReviewRepository;
import com.dut.CinemaProject.dao.repos.UserRepository;
import com.dut.CinemaProject.dto.Review.NewReview;
import com.dut.CinemaProject.dto.Review.ReviewDto;
import com.dut.CinemaProject.exceptions.BadRequestException;
import com.dut.CinemaProject.exceptions.ItemNotFoundException;
import com.dut.CinemaProject.services.interfaces.IReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public ReviewDto createReview(NewReview newReview){
        User user = userRepository.findById(newReview.getAuthorId())
                .orElseThrow(() -> new ItemNotFoundException("Such user does not exist"));

        Movie movie = movieRepository.findById(newReview.getMovieId())
                .orElseThrow(()-> new ItemNotFoundException("Such movie does not exist"));

        if(newReview.getText().isBlank())
            throw new BadRequestException("Text is empty");

        if (newReview.getText().length() > 255)
            throw new BadRequestException("Text is too long!");

        Review review = new Review();
        review.setAuthor(user);
        review.setMovie(movie);
        review.setText(newReview.getText());
        review.setCreationDate(LocalDateTime.now());

        return new ReviewDto(reviewRepository.save(review));
    }

    @Override
    public ReviewDto getById(Long id) {
        return new ReviewDto(reviewRepository.findById(id)
                .orElseThrow(() -> new com.dut.CinemaProject.exceptions.ItemNotFoundException("Review not found")));
    }

    @Override
    public List<ReviewDto> getReviewsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(ItemNotFoundException::new);
        return reviewRepository.findReviewsByMovie(movie).stream().map(ReviewDto::new).collect(Collectors.toList());
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        reviewRepository.delete(review);
    }

    @Override
    public ReviewDto updateReview(Long id, String newText) {
        Review reviewDb = reviewRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Review not found"));

        if(newText.isBlank())
            throw new BadRequestException("Text is empty");

        if (newText.length() > 255)
            throw new BadRequestException("Text is too long!");

        reviewDb.setText(newText);

        return new ReviewDto(reviewRepository.save(reviewDb));
    }

    @Override
    public List<ReviewDto> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }
}
