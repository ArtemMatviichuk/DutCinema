package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dto.Review.NewReview;
import com.dut.CinemaProject.dto.Review.ReviewDto;
import com.dut.CinemaProject.services.interfaces.IReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getAll(){
        return reviewService.getAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto getById(@PathVariable Long id){
        return reviewService.getById(id);
    }

    @GetMapping("movie/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewDto> getListOfMovieReviews(@PathVariable Long id){
        return reviewService.getReviewsByMovie(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto addReview(@RequestBody NewReview newReview){
        return reviewService.createReview(newReview);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewDto editReview(@PathVariable Long id, @RequestBody String newText){
        return reviewService.updateReview(id, newText);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id){
        reviewService.deleteReview(id);
    }
}
