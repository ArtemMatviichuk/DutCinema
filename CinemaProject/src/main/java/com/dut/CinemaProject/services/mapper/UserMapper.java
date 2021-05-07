package com.dut.CinemaProject.services.mapper;

import com.dut.CinemaProject.dao.domain.User;
import com.dut.CinemaProject.dto.User.UserRegisterData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User userFromRegisterData(UserRegisterData userRegisterData){

        User user = new User();
        user.setFirstName(userRegisterData.getFirstName());
        user.setLastName(userRegisterData.getLastName());
        user.setEmail(userRegisterData.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterData.getPassword()));

        return user;
    }
}
