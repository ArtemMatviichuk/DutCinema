package com.dut.CinemaProject.dto.Hall;

import com.dut.CinemaProject.dao.domain.Hall;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallDto {

    public HallDto(Hall hall) {
        this.id = hall.getId();
        this.name = hall.getName();
        this.rowsAmount = hall.getRowsAmount();
        this.places = hall.getPlaces();
    }

    private Long id;
    private String name;
    private Integer rowsAmount;
    private Integer places;
}
