package com.dut.CinemaProject.services.services;

import com.dut.CinemaProject.dao.domain.JwtBlacklist;
import com.dut.CinemaProject.dao.domain.Role;
import com.dut.CinemaProject.dao.domain.Status;
import com.dut.CinemaProject.dao.domain.User;
import com.dut.CinemaProject.dao.repos.RoleRepository;
import com.dut.CinemaProject.dao.repos.UserRepository;
import com.dut.CinemaProject.dto.User.AuthenticationRequestDto;
import com.dut.CinemaProject.dto.User.UserDto;
import com.dut.CinemaProject.dto.User.UserRegisterData;
import com.dut.CinemaProject.exceptions.*;
import com.dut.CinemaProject.security.jwt.JwtTokenProvider;
import com.dut.CinemaProject.services.interfaces.IUserService;
import com.dut.CinemaProject.services.mapper.UserMapper;

import lombok.AllArgsConstructor;

import org.passay.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    public Map<String, String> login(AuthenticationRequestDto requestDto) {

        String email = requestDto.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        checkUserStatus(user);
        user.setStatus(Status.ACTIVE);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));


        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("id", user.getId().toString());
        response.put("token", jwtTokenProvider.createToken(email, user.getRoles()));

        return response;
    }

    @Override
    public void logout(Map<String, String> json) {
        String token = json.get("token");
        JwtBlacklist jwtBlacklist = new JwtBlacklist();
        jwtBlacklist.setToken(token);

        User user = userRepository.findById(Long.parseLong(json.get("id")))
                .orElseThrow(() -> new UserNotFoundException("No such user in database"));

        user.setStatus(Status.NOT_ACTIVE);
        userRepository.save(user);

        jwtBlacklistService.saveTokenToBlacklist(jwtBlacklist);
    }

    private void checkUserStatus(User user) {
        if(user.getStatus().name().equals("ACTIVE"))
            throw new JwtAuthenticationException("User is already logged in");

        if(user.getStatus().name().equals("BLOCKED"))
            throw new JwtAuthenticationException("User is blocked");
    }

    @Override
    public UserDto register(UserRegisterData userRegisterData) {

        isEmailFree(userRegisterData.getEmail());

        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        User user = UserMapper.userFromRegisterData(userRegisterData);

        user.setRoles(userRoles);
        user.setStatus(Status.NOT_ACTIVE);
        user.setCreated(new Date());

        return new UserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> result = userRepository.findByEmail(username);
        return result.get();
    }

    @Override
    public UserDto findById(Long id) {
        return new UserDto(userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found")));
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean isEmailFree(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent())
            throw new EmailAlreadyExistsException("This email is already registered");

        return true;
    }

    @Override
    public String changeUserPasswordById(Long userId, String newPassword, String oldPassword) {
        List<Rule> rules = new ArrayList<>();
        rules.add(new WhitespaceRule());
        rules.add(new LengthRule(4, 32));
        rules.add(new AllowedRegexRule("^[A-Za-z0-9]+$"));

        User user = userRepository.findById(userId).orElseThrow(ItemNotFoundException::new);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("src/main/resources/messages.properties"));
        } catch (IOException e) {
            throw new FileSystemNotFoundException("System cannot find validator configuration (messages.properties). " +
                    "Try to change path.");
        }

        MessageResolver resolver = new PropertiesMessageResolver(props);

        PasswordValidator passwordValidator = new PasswordValidator(resolver, rules);
        PasswordData passwordData = new PasswordData(newPassword);
        RuleResult ruleResult = passwordValidator.validate(passwordData);

        if(!passwordEncoder.matches(oldPassword ,user.getPassword())) {
            throw new ValidationException("Password doesn't exist!");
        }

        if(newPassword.equals(oldPassword)) {
            throw new ValidationException("Password must not be the same as old!");
        }

        if(ruleResult.isValid()) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            return "Password has been successfully changed!";
        }
        else {
            throw new ValidationException(passwordValidator.getMessages(ruleResult).toString());
        }
    }

    @Override
    public UserDto blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such user in database"));

        user.setStatus(user.getStatus().equals(Status.BLOCKED) ? Status.NOT_ACTIVE : Status.BLOCKED);
        return new UserDto(userRepository.save(user));
    }

    @Override
    public UserDto changeAdminStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such user in database"));

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");

        if (user.getRoles().contains(roleAdmin))
            user.getRoles().remove(roleAdmin);
        else
            user.getRoles().add(roleAdmin);

        return new UserDto(userRepository.save(user));
    }

}

