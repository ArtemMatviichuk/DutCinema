package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, Long> {
    List<Hall> findByName(String name);
    List<Hall> findHallByIsBlocked(Boolean isBlocked);
}
