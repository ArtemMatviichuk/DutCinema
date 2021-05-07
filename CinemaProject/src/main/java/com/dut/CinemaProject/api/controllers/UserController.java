package com.dut.CinemaProject.api.controllers;

import com.dut.CinemaProject.dto.User.ChangePassword;
import com.dut.CinemaProject.dto.User.UserDto;
import com.dut.CinemaProject.services.interfaces.IAccountService;
import com.dut.CinemaProject.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final IAccountService accountService;
    private final IUserService userService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto showAccount(@PathVariable Long id){
        return (accountService.getUserAccount(id));
    }

    @PutMapping("{userId}/changePassword")
    @ResponseStatus(HttpStatus.OK)
    public String changePassword(@PathVariable Long userId, @RequestBody ChangePassword changePassword){
        return userService.changeUserPasswordById(userId, changePassword.getNewPassword(),
                changePassword.getOldPassword());

    }

    @PutMapping("{id}/block")
    @ResponseStatus(HttpStatus.OK)
    public UserDto blockUser(@PathVariable Long id){
        return userService.blockUser(id);
    }

    @PutMapping("{id}/changeAdminStatus")
    @ResponseStatus(HttpStatus.OK)
    public UserDto changeAdminStatus(@PathVariable Long id){
        return userService.changeAdminStatus(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(){
        return userService.getAll();
    }

}
