package com.dut.CinemaProject.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Getter @Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer row;
    private Integer place;
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;
}
