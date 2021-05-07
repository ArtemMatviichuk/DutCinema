package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dto.Hall.HallDto;
import com.dut.CinemaProject.dto.Hall.NewHall;
import com.dut.CinemaProject.dto.Hall.UpdateHallData;

import java.util.List;

public interface IHallService {
    HallDto createHall(NewHall newHall);
    void deleteHall(Long id);
    HallDto updateHall(Long id, UpdateHallData hall);
    HallDto getHallById(Long id);
    List<HallDto> getAllHalls();
    void blockHall(Long id);
    void unblockHall(Long id);
    List<HallDto> getAllBlockedHalls();
    List<HallDto> getAllActiveHalls();
    boolean isNameFree(String name);
}
