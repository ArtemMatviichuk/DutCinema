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
public class SessionShort {
    public SessionShort(Session session){
        this.id = session.getId();
        this.hallId = session.getHall().getId();
        this.hallName = session.getHall().getName();
        this.date = session.getDate();
    }

    private Long id;
    private Long hallId;
    private String hallName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd-MM-yyyy")
    private LocalDateTime date;
}
