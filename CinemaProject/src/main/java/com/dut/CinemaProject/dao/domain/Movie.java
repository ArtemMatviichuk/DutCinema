package com.dut.CinemaProject.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "movies")
@Getter @Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    @Column(name="poster_path")
    private String posterPath;
    @Column(name="trailer_path")
    private String trailerPath;
    private Integer duration;
    private String actors;
    private String country;
    @Column(name="is_blocked")
    private Boolean isBlocked;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movie_genres",
            joinColumns = {@JoinColumn(name = "movie_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id", referencedColumnName = "id")})
    private Set<Genre> genres;
}
