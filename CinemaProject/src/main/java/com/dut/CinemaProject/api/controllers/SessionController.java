package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dto.Session.SessionData;
import com.dut.CinemaProject.dto.Session.SessionDto;
import com.dut.CinemaProject.dto.Session.SessionTicketsList;
import com.dut.CinemaProject.services.interfaces.ISessionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("sessions")
@AllArgsConstructor
public class SessionController {
    private ISessionService sessionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SessionDto> getSessions(){
        return sessionService.getAll();
    }

    @GetMapping("actual")
    @ResponseStatus(HttpStatus.OK)
    public List<SessionDto> getActual(){
        return sessionService.getActualSessions();
    }

    @GetMapping("{id}/tickets")
    @ResponseStatus(HttpStatus.OK)
    public SessionTicketsList getTicketsList(@PathVariable Long id){
        return sessionService.getSessionTicketsData(id);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto getSession(@PathVariable Long id){
        return sessionService.getSession(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto addSession(@RequestBody SessionData sessionData){
        return sessionService.createSession(sessionData);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto updateSession(@PathVariable Long id, @RequestBody SessionData sessionData){
        return sessionService.updateSession(id, sessionData);
    }

    @PostMapping("{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public SessionDto cancelSession(@PathVariable Long id){
        return sessionService.cancelSession(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSession(@PathVariable Long id){
        sessionService.removeSession(id);
    }
}
