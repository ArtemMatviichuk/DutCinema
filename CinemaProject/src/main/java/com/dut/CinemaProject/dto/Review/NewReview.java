package com.dut.CinemaProject.dto.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NewReview {
    private String text;
    private Long movieId;
    private Long authorId;
}
