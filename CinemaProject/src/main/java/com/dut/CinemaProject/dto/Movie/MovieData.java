package com.dut.CinemaProject.dto.Movie;

import com.dut.CinemaProject.dao.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class MovieData {
    private String title;
    private String description;
    private String posterPath;
    private String trailerPath;
    private Integer duration;
    private String actors;
    private Set<Genre> genres;
    private String country;
}
