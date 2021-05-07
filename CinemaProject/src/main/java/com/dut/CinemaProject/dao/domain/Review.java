package com.dut.CinemaProject.dao.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter @Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

    private String text;
    @Column(name="creation_date")
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
}
