package com.dut.CinemaProject.dto.Ticket;

import com.dut.CinemaProject.dao.domain.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TicketDto {
    public TicketDto(Ticket ticket){
        this.id = ticket.getId();
        this.sessionId = ticket.getSession().getId();
        this.movieId = ticket.getSession().getMovie().getId();
        this.movieTitle = ticket.getSession().getMovie().getTitle();
        this.date = ticket.getSession().getDate();
        this.hallName = ticket.getSession().getHall().getName();
        this.row = ticket.getRow();
        this.place = ticket.getPlace();
        this.posterPath = ticket.getSession().getMovie().getPosterPath();
        this.isCanceled = ticket.getSession().getIsCanceled();
    }

    private Long id;
    private Long sessionId;
    private Long movieId;
    private String movieTitle;
    private LocalDateTime date;
    private String hallName;
    private Integer row;
    private Integer place;
    private String posterPath;
    private Boolean isCanceled;
}
