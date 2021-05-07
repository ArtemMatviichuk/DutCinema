package com.dut.CinemaProject.dao.repos;

import com.dut.CinemaProject.dao.domain.JwtBlacklist;
import com.dut.CinemaProject.dao.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlacklistRepository  extends JpaRepository<JwtBlacklist, Long> {
    JwtBlacklist findByTokenEquals(String token);
}
