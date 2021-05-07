package com.dut.CinemaProject.utils;

import com.dut.CinemaProject.dao.domain.Movie;
import com.dut.CinemaProject.dao.domain.Review;
import com.dut.CinemaProject.dao.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

public class ReviewsGenerator {
    @Getter
    public static class Builder {
        private Review newReview;

        public Builder() {
            newReview = new Review();
        }

        public Builder withId(Long id) {
            newReview.setId(id);
            return this;
        }

        public Builder withText(String text) {
            newReview.setText(text);
            return this;
        }

        public Builder withCreationDate(LocalDateTime creationDate) {
            newReview.setCreationDate(creationDate);
            return this;
        }

        public Builder withMovie(Movie movie) {
            newReview.setMovie(movie);
            return this;
        }

        public Builder withAuthor(User author) {
            newReview.setAuthor(author);
            return this;
        }


        public Review build() {
            return newReview;
        }
    }
}