package com.dut.CinemaProject.dto.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseTicketsList {
    private Long sessionId;
    private Long userId;
    private List<Place> places;
}
