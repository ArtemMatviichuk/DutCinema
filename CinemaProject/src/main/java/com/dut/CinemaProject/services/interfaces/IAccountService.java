package com.dut.CinemaProject.services.interfaces;

import com.dut.CinemaProject.dto.User.UserDto;

public interface IAccountService {
    UserDto getUserAccount(Long id);
}
