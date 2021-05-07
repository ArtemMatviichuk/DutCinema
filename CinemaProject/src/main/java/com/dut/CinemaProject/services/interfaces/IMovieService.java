package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.dto.Movie.MovieData;
import com.dut.CinemaProject.dto.Movie.MovieDto;
import com.dut.CinemaProject.dto.Movie.MoviePage;
import com.dut.CinemaProject.dto.Movie.MovieTitle;
import com.dut.CinemaProject.dto.Session.SessionShort;

import java.util.List;
import java.util.Map;

public interface IMovieService {
    MovieDto createMovie(MovieData newMovie);
    void deleteMovie(Long id);
    MovieDto updateMovie(Long id, MovieData movie);
    MovieDto getMovieById(Long id);
    List<SessionShort> getSessions(Long id);
    MoviePage getMovies(Integer page, Integer perPage, List<Genre> genres, String title);
    Integer getPagesAmount(Integer perPage, List<Genre> genres, String title);
    List<MovieTitle> getTitles();
    List<MovieDto> getAll();
    void blockMovie(Long id);
    void unblockMovie(Long id);
    List<MovieDto> getAllBlockedMovies();
}
