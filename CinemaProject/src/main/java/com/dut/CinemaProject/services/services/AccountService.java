package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.repos.UserRepository;
import com.dut.CinemaProject.dto.User.UserDto;
import com.dut.CinemaProject.exceptions.ItemNotFoundException;
import com.dut.CinemaProject.services.interfaces.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService {

    final private UserRepository userRepository;

    @Override
    public UserDto getUserAccount(Long id) {
        return new UserDto(userRepository.findById(id).orElseThrow(()-> new ItemNotFoundException("User not found")));
    }
}
