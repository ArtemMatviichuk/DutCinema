package com.dut.CinemaProject.api.controllers;


import com.dut.CinemaProject.dto.Hall.HallDto;
import com.dut.CinemaProject.dto.Hall.NewHall;
import com.dut.CinemaProject.dto.Hall.UpdateHallData;
import com.dut.CinemaProject.services.interfaces.IHallService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("halls")
@AllArgsConstructor
public class HallController {

    private final IHallService hallService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public HallDto getById(@PathVariable Long id){
        return hallService.getHallById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HallDto addHall(@RequestBody NewHall newHall){
        return hallService.createHall(newHall);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHall(@PathVariable Long id){
        hallService.deleteHall(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public HallDto updateHall(@PathVariable Long id, @RequestBody UpdateHallData hall){
        return  hallService.updateHall(id, hall);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HallDto> getAll(){
        return hallService.getAllHalls();
    }

    @PutMapping("block/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void blockHall(@PathVariable Long id){
        hallService.blockHall(id);
    }
    @PutMapping("unblock/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void unblockHall(@PathVariable Long id){
        hallService.unblockHall(id);
    }
    @GetMapping("blocked")
    @ResponseStatus(HttpStatus.OK)
    public List<HallDto> getBlocked() {
        return hallService.getAllBlockedHalls();
    }
    @GetMapping("active")
    @ResponseStatus(HttpStatus.OK)
    public List<HallDto> getActive() {
        return hallService.getAllActiveHalls();
    }
    @GetMapping("check-{name}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isNameFree(@PathVariable String name){
        return hallService.isNameFree(name);
    }
}
