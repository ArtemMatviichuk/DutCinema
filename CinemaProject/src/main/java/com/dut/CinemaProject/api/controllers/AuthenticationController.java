package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dto.User.AuthenticationRequestDto;
import com.dut.CinemaProject.dto.User.UserDto;
import com.dut.CinemaProject.dto.User.UserRegisterData;
import com.dut.CinemaProject.services.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> login(@RequestBody AuthenticationRequestDto requestDto){
        return userService.login(requestDto);
    }

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestBody Map<String, String> json){
        userService.logout(json);
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.OK)
    public UserDto register(@RequestBody UserRegisterData userRegisterData){
        return userService.register(userRegisterData);
    }

    @GetMapping("check-{email}")
    @ResponseStatus(HttpStatus.OK)
    public void isEmailFree(@PathVariable String email){
        userService.isEmailFree(email);
    }

    @GetMapping("is-admin-true")
    @ResponseStatus(HttpStatus.OK)
    public boolean isAdminTrue(){
        return true;
    }

}
