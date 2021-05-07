package com.dut.CinemaProject.dto.Hall;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateHallData {
    private Long id;
    private String name;
    private Integer rowsAmount;
    private Integer places;
}
