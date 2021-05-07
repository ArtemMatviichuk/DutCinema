package com.dut.CinemaProject.dto.Session;

import com.dut.CinemaProject.dto.Ticket.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionTicketsList {
    private Long sessionId;
    private Integer rowsAmount;
    private Integer place;
    private List<Place> tickets;
}
