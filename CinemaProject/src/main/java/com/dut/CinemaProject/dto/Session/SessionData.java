package com.dut.CinemaProject.dto.Session;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessionData {
    private Long movieId;
    private Long hallId;
    private LocalDateTime date;
}
