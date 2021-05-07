package com.dut.CinemaProject.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Getter @Setter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;
    @Column(name = "is_canceled")
    private Boolean isCanceled;
}
