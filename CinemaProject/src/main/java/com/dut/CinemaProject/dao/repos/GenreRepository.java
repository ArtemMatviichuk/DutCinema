package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g FROM Genre g WHERE g.name = :name")
    Optional<Genre> findByName(String name);
}

