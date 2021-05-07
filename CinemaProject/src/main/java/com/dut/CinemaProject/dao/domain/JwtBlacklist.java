package com.dut.CinemaProject.dao.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "jwt_blacklist")
@Data
@EqualsAndHashCode
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String token;
}
