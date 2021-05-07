package com.dut.CinemaProject.dto.Movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoviePage {
    private List<MovieDto> movies;
    private Integer pages;
}
