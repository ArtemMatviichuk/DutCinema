package com.dut.CinemaProject.dto.User;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterData {

    @NotBlank(message = "First name is mandatory")
    @Min(3)
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Min(3)
    @Pattern(regexp = "[a-zA-Zа-яА-Я]+")
    private String lastName;

    @NotBlank(message = "Password is mandatory")
    @Min(8)
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")
    private String email;
}
