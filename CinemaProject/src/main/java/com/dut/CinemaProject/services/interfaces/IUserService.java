package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dao.domain.User;
import com.dut.CinemaProject.dto.User.AuthenticationRequestDto;
import com.dut.CinemaProject.dto.User.UserDto;
import com.dut.CinemaProject.dto.User.UserRegisterData;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IUserService {

    Map<String, String> login(AuthenticationRequestDto requestDto);

    void logout(Map<String, String> token);

    UserDto register(UserRegisterData userRegisterData);

    List<UserDto> getAll();

    User findByUsername(String username);

    UserDto findById(Long id);

    void delete(Long id);

    String changeUserPasswordById(Long userId, String newPassword, String oldPassword);

    UserDto blockUser(Long id);
    UserDto changeAdminStatus(Long id);

}
