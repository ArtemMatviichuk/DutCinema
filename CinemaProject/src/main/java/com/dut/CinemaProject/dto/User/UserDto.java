package com.dut.CinemaProject.dto.User;

import com.dut.CinemaProject.dao.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.dut.CinemaProject.dao.domain.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dut.CinemaProject.dao.domain.Status.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String status;
    private List<String> roles;

    public UserDto(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        switch (user.getStatus()){
            case ACTIVE:
                this.status = "Active";
                break;
            case NOT_ACTIVE:
                this.status = "Not active";
                break;
            case BLOCKED:
                this.status = "Blocked";
                break;
            default:
                this.status = "";
                break;
        }
        this.roles = user.getRoles()
                .stream()
                .map((u) -> u.getName().replace("ROLE_", ""))
                .collect(Collectors.toList());
    }

}
