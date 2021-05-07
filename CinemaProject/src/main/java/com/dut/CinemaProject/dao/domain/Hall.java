package com.dut.CinemaProject.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "halls")
@Getter @Setter
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer rowsAmount;
    private Integer places;
    @Column(name="is_blocked")
    private Boolean isBlocked;
}
