package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dao.domain.Genre;

import java.util.List;

public interface IGenreService {
    Genre createGenre(Genre genre);
    void deleteGenre(Long id);
    Genre updateGenre(Long id, Genre genre);
    Genre getGenreById(Long id);
    List<Genre> getGenres();
    boolean isNameFree(String name);
}
