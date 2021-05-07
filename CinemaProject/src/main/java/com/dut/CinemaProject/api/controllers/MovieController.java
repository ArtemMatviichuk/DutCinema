package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.dto.Movie.MovieData;
import com.dut.CinemaProject.dto.Movie.MovieDto;
import com.dut.CinemaProject.dto.Movie.MoviePage;
import com.dut.CinemaProject.dto.Movie.MovieTitle;
import com.dut.CinemaProject.dto.Session.SessionShort;
import com.dut.CinemaProject.services.interfaces.IMovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("movies")
@AllArgsConstructor
public class MovieController {

    private final IMovieService movieService;

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDto> getAll(){
        return movieService.getAll();
    }
    @GetMapping("blocked")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieDto> getBlocked() {
        return movieService.getAllBlockedMovies();
    }

    @GetMapping("titles")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieTitle> getTitles(){
        return movieService.getTitles();
    }

    @PostMapping("pages")
    @ResponseStatus(HttpStatus.OK)
    public MoviePage getMovies(@RequestParam(name = "page", defaultValue = "1") Integer page,
                               @RequestParam(name = "perPage", defaultValue = "20") Integer perPage,
                               @RequestBody List<Genre> genres,
                               @RequestParam(name = "title", defaultValue = "") String title){
        return movieService.getMovies(page, perPage, genres, title);
    }

    @PostMapping("pages/{perPage}")
    @ResponseStatus(HttpStatus.OK)
    public Integer pageAmount(@PathVariable Integer perPage,
                              @RequestBody List<Genre> genres,
                              @RequestParam(name = "title", defaultValue = "") String title){
        return movieService.getPagesAmount(perPage, genres, title);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDto getById(@PathVariable Long id){
        return movieService.getMovieById(id);
    }

    @GetMapping("{id}/sessions")
    public List<SessionShort> getSessions(@PathVariable Long id){
        return movieService.getSessions(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovieDto addMovie(@RequestBody MovieData newMovie){
        return movieService.createMovie(newMovie);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Long id){
        movieService.deleteMovie(id);
    }

    @PutMapping("block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void blockMovie(@PathVariable Long id){
        movieService.blockMovie(id);
    }
    @PutMapping("unblock/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unblockMovie(@PathVariable Long id){
        movieService.unblockMovie(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public MovieDto updateMovie(@PathVariable Long id, @RequestBody MovieData movie){
        return  movieService.updateMovie(id, movie);
    }
}
