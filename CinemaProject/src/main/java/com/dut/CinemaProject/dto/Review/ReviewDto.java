package com.dut.CinemaProject.dto.Review;

import com.dut.CinemaProject.dao.domain.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReviewDto {
    public ReviewDto(Review review){
        this.id = review.getId();
        this.movieId = review.getMovie().getId();
        this.movieTitle = review.getMovie().getTitle();
        this.authorId = review.getAuthor().getId();
        this.firstName = review.getAuthor().getFirstName();
        this.lastName = review.getAuthor().getLastName();
        this.text = review.getText();
        this.creationDate = review.getCreationDate();
    }

    private Long id;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime creationDate;
    private Long movieId;
    private String movieTitle;
    private Long authorId;
    private String firstName;
    private String lastName;
}
