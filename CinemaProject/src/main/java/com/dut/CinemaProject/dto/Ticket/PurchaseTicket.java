package com.dut.CinemaProject.dto.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseTicket {
    public PurchaseTicket(Long userId, Long sessionId, Integer row, Integer place){
        this.userId = userId;
        this.sessionId = sessionId;
        this.row = row;
        this.place = place;
    }

    private Long sessionId;
    private Long userId;
    private Integer row;
    private Integer place;
}