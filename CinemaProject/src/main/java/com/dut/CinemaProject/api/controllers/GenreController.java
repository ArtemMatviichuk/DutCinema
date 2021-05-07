package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.services.interfaces.IGenreService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("genres")
public class GenreController {

    private final IGenreService genreService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getById(@PathVariable Long id){
        return genreService.getGenreById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Genre addGenre(@RequestBody Genre genre){
        return genreService.createGenre(genre);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(@PathVariable Long id){
        genreService.deleteGenre(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre updateGenre(@PathVariable Long id, @RequestBody Genre genre){
        return  genreService.updateGenre(id, genre);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAll(){
        return genreService.getGenres();
    }

    @GetMapping("check-{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isNameFree(@PathVariable String name){
        return genreService.isNameFree(name);
    }
}