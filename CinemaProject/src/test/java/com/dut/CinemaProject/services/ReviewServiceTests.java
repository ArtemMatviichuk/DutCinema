package com.dut.CinemaProject.services;

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
import com.dut.CinemaProject.exceptions.ValidationException;
import com.dut.CinemaProject.services.services.ReviewService;
import com.dut.CinemaProject.services.services.UserService;
import com.dut.CinemaProject.utils.ReviewsGenerator;
import com.dut.CinemaProject.utils.UsersGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReviewServiceTests {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    UserRepository userRepository;

    @Mock
    MovieRepository movieRepository;

    @Mock
    ReviewRepository reviewRepository;

    @Test
    public void createReview_customUserNotExist_shouldThrowItemNotFoundException() {
        NewReview newReview = new NewReview("Test", 1L, 1L);
        when(userRepository.findById(newReview.getAuthorId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                reviewService.createReview(newReview)
        );

        assertTrue(exception.getMessage().contains("Such user does not exist"));
    }

    @Test
    public void createReview_customUserExistAndCustomMovieNotExist_shouldThrowItemNotFoundException() {
        NewReview newReview = new NewReview("Test", 1L, 1L);
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        when(userRepository.findById(newReview.getAuthorId())).thenReturn(Optional.of(user));
        when(movieRepository.findById(newReview.getMovieId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                reviewService.createReview(newReview)
        );

        assertTrue(exception.getMessage().contains("Such movie does not exist"));
    }

    @Test
    public void createReview_customUserExistAndCustomMovieExistAndNewReviewHaveWhitespaceInText_shouldThrowBadRequestException() {
        NewReview newReview = new NewReview(" ", 1L, 1L);
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        Movie movie = new Movie();
        movie.setId(1L);
        when(userRepository.findById(newReview.getAuthorId())).thenReturn(Optional.of(user));
        when(movieRepository.findById(newReview.getMovieId())).thenReturn(Optional.of(movie));

        Exception exception = assertThrows(BadRequestException.class, ()->
                reviewService.createReview(newReview)
        );

        assertTrue(exception.getMessage().contains("Text is empty"));
    }

    @Test
    public void createReview_customUserExistAndCustomMovieNotExist_shouldShouldBeCallOnce() {
        NewReview newReview = new NewReview("Test", 1L, 1L);
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        Movie movie = new Movie();
        movie.setId(1L);
        when(userRepository.findById(newReview.getAuthorId())).thenReturn(Optional.of(user));
        when(movieRepository.findById(newReview.getMovieId())).thenReturn(Optional.of(movie));
        Review review = new Review();
        review.setId(1L);
        review.setAuthor(user);
        review.setMovie(movie);
        when(reviewRepository.save(any())).thenReturn(review);

        reviewService.createReview(newReview);

        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    public void getById_shouldThrowItemNotFoundException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ItemNotFoundException.class, ()->
                reviewService.getById(1L)
        );

        assertTrue(exception.getMessage().contains("Review not found"));
    }

    @Test
    public void getById_shouldBeCallOnce() {
        Movie movie = new Movie();
        movie.setId(1L);
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        Review review = new ReviewsGenerator.Builder()
                .withAuthor(user)
                .withMovie(movie)
                .withId(1L)
                .build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        reviewService.getById(1L);

        verify(reviewRepository, times(1)).findById(any());
    }

    @Test(expected = ItemNotFoundException.class)
    public void getReviewsByMovie_shouldThrowItemNotFoundException() {
        reviewService.getById(anyLong());
    }

    @Test(expected = ItemNotFoundException.class)
    public void getReviewsByMovie_shouldBeCallOnce() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(new Movie()));

        reviewService.getById(1L);

        verify(reviewRepository, times(1)).findReviewsByMovie(any());
    }

    @Test(expected = ItemNotFoundException.class)
    public void deleteReview_shouldThrowItemNotFoundException() {
        reviewService.deleteReview(anyLong());
    }

    @Test
    public void deleteReview_shouldBeCallOnce() {
        Movie movie = new Movie();
        movie.setId(1L);
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .build();
        Review review = new ReviewsGenerator.Builder()
                .withAuthor(user)
                .withMovie(movie)
                .withId(1L)
                .build();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

        reviewService.deleteReview(anyLong());

        verify(reviewRepository, times(1)).delete(any());
    }
}
