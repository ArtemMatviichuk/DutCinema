package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dto.Session.SessionData;
import com.dut.CinemaProject.dto.Session.SessionDto;
import com.dut.CinemaProject.dto.Session.SessionTicketsList;

import java.util.List;

public interface ISessionService {
    SessionTicketsList getSessionTicketsData(Long sessionId);
    SessionDto getSession(Long id);
    List<SessionDto> getActualSessions();

    SessionDto createSession(SessionData sessionData);
    void removeSession(Long id);
    SessionDto updateSession(Long id, SessionData sessionData);
    List<SessionDto> getAll();

    SessionDto cancelSession(Long id);
}
