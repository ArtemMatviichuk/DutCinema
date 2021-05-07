package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.dao.repos.GenreRepository;
import com.dut.CinemaProject.exceptions.BadRequestException;
import com.dut.CinemaProject.exceptions.ItemNotFoundException;
import com.dut.CinemaProject.services.interfaces.IGenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GenreService implements IGenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre createGenre(Genre genre) {
        if(genre.getName().isBlank())
            throw new BadRequestException("Name can`t be empty");
        else if (genreRepository.findByName(genre.getName()).isPresent())
            throw new BadRequestException("This name is already used");

        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Genre is not found"));
        genreRepository.delete(genre);
    }

    @Override
    public Genre updateGenre(Long id, Genre genre) {
        Genre updateGenre = genreRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Genre is not found"));

        if(genre.getName() != null){
            if(genre.getName().isBlank())
                throw new BadRequestException("Name can`t be empty");
            else if(genreRepository.findByName(genre.getName()).isPresent() && !updateGenre.getName().equals(genre.getName()))
                throw new BadRequestException("This name is already used");
            else
                updateGenre.setName(genre.getName());
        }

        return genreRepository.save(updateGenre);
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Genre is not found"));
    }

    @Override
    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    @Override
    public boolean isNameFree(String name) {
        Optional<Genre> genre = genreRepository.findByName(name);
        if(genre.isPresent())
            return false;

        return true;
    }
}