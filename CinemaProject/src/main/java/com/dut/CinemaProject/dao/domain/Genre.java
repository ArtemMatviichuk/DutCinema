package com.dut.CinemaProject.dao.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    private Set<Movie> movies;

    @Override
    public boolean equals(Object obj) {
        return this.id.equals(((Genre) obj).getId());
    }

    @Override
    public int hashCode() {
        return Math.toIntExact(this.id);
    }
}
