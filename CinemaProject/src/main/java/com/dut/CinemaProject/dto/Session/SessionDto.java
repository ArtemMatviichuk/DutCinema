package com.dut.CinemaProject.dto.Session;

import com.dut.CinemaProject.dao.domain.Session;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SessionDto {
    public SessionDto(Session session){
        this.id = session.getId();
        this.movieId = session.getMovie().getId();
        this.hallId = session.getHall().getId();
        this.movieTitle = session.getMovie().getTitle();
        this.moviePoster = session.getMovie().getPosterPath();
        this.hallName = session.getHall().getName();
        this.date = session.getDate();
        this.isCanceled = session.getIsCanceled();
    }

    private Long id;
    private Long movieId;
    private Long hallId;
    private String movieTitle;
    private String moviePoster;
    private String hallName;
    private LocalDateTime date;
    private Boolean isCanceled;
}
