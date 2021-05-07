package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.domain.Genre;
import com.dut.CinemaProject.dao.domain.Movie;
import com.dut.CinemaProject.dao.repos.MovieRepository;
import com.dut.CinemaProject.dao.repos.SessionRepository;
import com.dut.CinemaProject.dto.Movie.MovieData;
import com.dut.CinemaProject.dto.Movie.MovieDto;
import com.dut.CinemaProject.dto.Movie.MoviePage;
import com.dut.CinemaProject.dto.Movie.MovieTitle;
import com.dut.CinemaProject.dto.Session.SessionShort;
import com.dut.CinemaProject.exceptions.BadRequestException;
import com.dut.CinemaProject.exceptions.ItemNotFoundException;
import com.dut.CinemaProject.services.interfaces.IMovieService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService implements IMovieService {

    private final MovieRepository movieRepository;
    private final SessionRepository sessionRepository;

    @Override
    public MovieDto createMovie(MovieData newMovie) {
        if (newMovie.getTitle().isBlank() || newMovie.getDescription().isBlank()
                || newMovie.getPosterPath().isBlank() || newMovie.getActors().isBlank()
                || newMovie.getCountry().isBlank())
            throw new BadRequestException("Information can`t be empty");
        if (newMovie.getDuration() <= 0)
            throw new BadRequestException("Time can`t be less than 1");

        Movie movie = new Movie();
        movie.setTitle(newMovie.getTitle());
        movie.setDescription(newMovie.getDescription());
        movie.setTrailerPath(newMovie.getTrailerPath());
        movie.setPosterPath(newMovie.getPosterPath());
        movie.setDuration(newMovie.getDuration());
        movie.setActors(newMovie.getActors());
        movie.setCountry(newMovie.getCountry());
        movie.setGenres(newMovie.getGenres());
        movie.setIsBlocked(false);

        return new MovieDto(movieRepository.save(movie));
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }

    @Override
    public MovieDto updateMovie(Long id, MovieData movie) {
        Movie updateMovie = movieRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Movie not found"));

        if (movie.getTitle() != null) {
            if (movie.getTitle().isBlank())
                throw new BadRequestException("Title can`t be empty!");
            else
                updateMovie.setTitle(movie.getTitle());
        }
        if (movie.getDescription() != null) {
            if (movie.getDescription().isBlank())
                throw new BadRequestException("Description can`t be empty!");
            else
                updateMovie.setDescription(movie.getDescription());
        }
        if (movie.getTrailerPath() != null) {
            updateMovie.setTrailerPath(movie.getTrailerPath());
        }
        if (movie.getPosterPath() != null) {
            if (movie.getPosterPath().isBlank())
                throw new BadRequestException("PosterPath can`t be empty!");
            else
                updateMovie.setPosterPath(movie.getPosterPath());
        }
        if (movie.getDuration() != null) {
            if (movie.getDuration() <= 0)
                throw new BadRequestException("Time can`t be less than 1!");
            else
                updateMovie.setDuration(movie.getDuration());
        }
        if (movie.getGenres() != null) {
            updateMovie.setGenres(movie.getGenres());
        }
        if (movie.getCountry() != null) {
            if (movie.getCountry().isBlank())
                throw new BadRequestException("Country can`t be empty!");
            else
                updateMovie.setCountry(movie.getCountry());
        }
        if (movie.getActors() != null) {
            if (movie.getActors().isBlank())
                throw new BadRequestException("Actors can`t be empty!");
            else
                updateMovie.setActors(movie.getActors());
        }

        return new MovieDto(movieRepository.save(updateMovie));
    }

    @Override
    public MovieDto getMovieById(Long id) {
        return new MovieDto(movieRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Movie not found")));
    }

    @Override
    public List<SessionShort> getSessions(Long id) {
        if (!movieRepository.existsById(id))
            throw new ItemNotFoundException("Movie not found");

        return sessionRepository.getActualSessionsByMovieId(id)
                .stream()
                .map(SessionShort::new)
                .sorted((e1, e2) -> e1.getDate().isBefore(e2.getDate()) ? -1 : 1)
                .collect(Collectors.toList());
    }

    @Override
    public MoviePage getMovies(Integer page, Integer perPage, List<Genre> genres, String title) {
        if (page < 1)
            throw new BadRequestException("Page can not be less than 1!");

        if (perPage < 1)
            throw new BadRequestException("On page must be at list one element!");

        List<Movie> movies = movieRepository.findAll();

        if(!genres.isEmpty()){
            Set<Movie> genred = new LinkedHashSet<>();
            for (Genre genre: genres) {
                genred.addAll(movies.stream()
                        .filter(e -> e.getGenres().contains(genre))
                        .collect(Collectors.toList()));
            }
            movies = new ArrayList<>(genred);
        }

        if (!title.equals("") && movies.size() != 0)
            movies = movies.stream()
                    .filter(el -> el.getTitle()
                            .toLowerCase()
                            .contains(title.toLowerCase()))
                    .collect(Collectors.toList());

        if (movies.size() < (page - 1) * perPage)
            throw new BadRequestException("Requested page does not exist!");

        MoviePage result = new MoviePage();

        if (movies.size() >= page * perPage)
            result.setMovies(movies.subList((page - 1) * perPage, perPage * page)
                    .stream()
                    .map(MovieDto::new)
                    .collect(Collectors.toList()));
        else
            result.setMovies(movies.subList((page - 1) * perPage, movies.size())
                    .stream()
                    .map(MovieDto::new)
                    .collect(Collectors.toList()));

        double pages = (double) movies.size() / ((double) perPage);

        if (pages == (int) pages)
            result.setPages((int) pages);
        else
            result.setPages((int) (pages + 1));

        return result;
    }

    @Override
    public Integer getPagesAmount(Integer perPage, List<Genre> genres, String title) {
        if (perPage < 1)
            throw new BadRequestException("On page must be at list one element!");

        List<Movie> movies = movieRepository.findAll();

        if(!genres.isEmpty()){
            Set<Movie> genred = new LinkedHashSet<>();
            for (Genre genre: genres) {
                genred.addAll(movies.stream()
                        .filter(e -> e.getGenres().contains(genre))
                        .collect(Collectors.toList()));
            }
            movies = new ArrayList<>(genred);
        }

        if (!title.equals("") && movies.size() != 0)
            movies = movies.stream()
                    .filter(el -> el.getTitle()
                            .toLowerCase()
                            .contains(title.toLowerCase()))
                    .collect(Collectors.toList());

        double pages = (double) movies.size() / ((double) perPage);

        if (pages == (int) pages)
            return (int) pages;
        else
            return (int) (pages + 1);
    }

    @Override
    public List<MovieTitle> getTitles() {
        return movieRepository.findMovieByIsBlocked(false)
                .stream()
                .map(el -> new MovieTitle(el.getId(), el.getTitle()))
                .sorted((e1, e2) -> e1.getTitle().compareToIgnoreCase(e2.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getAll() {
        return movieRepository.findMovieByIsBlocked(false).stream().map(MovieDto::new).collect(Collectors.toList());
    }

    @Override
    public void blockMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Movie not found"));
        movie.setIsBlocked(true);
        movieRepository.save(movie);
    }

    @Override
    public void unblockMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Movie not found"));
        movie.setIsBlocked(false);
        movieRepository.save(movie);
    }

    @Override
    public List<MovieDto> getAllBlockedMovies() {
        return movieRepository.findMovieByIsBlocked(true).stream().map(MovieDto::new).collect(Collectors.toList());

    }
}
