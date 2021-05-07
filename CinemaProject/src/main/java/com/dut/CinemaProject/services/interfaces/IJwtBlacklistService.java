package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dao.domain.JwtBlacklist;

import java.util.Map;

public interface IJwtBlacklistService {
    JwtBlacklist findByTokenEquals(String token);
    JwtBlacklist saveTokenToBlacklist(JwtBlacklist jwtBlacklist);
}
