package com.dut.CinemaProject.services;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.dut.CinemaProject.services.services.JwtBlacklistService;
import com.dut.CinemaProject.services.services.UserService;
import com.dut.CinemaProject.utils.UsersGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    JwtBlacklistService jwtBlacklistService;

    @Test
    public void getAll_customUsersExists_shouldBeEqualExpected() {
        List<User> users = new ArrayList<>();

        users.add(new UsersGenerator.Builder()
                .withId(1L)
                .withEmail("Solo@email.com")
                .withFirstName("Alexei")
                .withLastName("Solo")
                .build()
        );

        users.add(new UsersGenerator.Builder()
                .withId(2L)
                .withEmail("user2@email.com")
                .withFirstName("Lokesh")
                .withLastName("Gupta")
                .build()
        );
        when(userRepository.findAll())
        .thenReturn(users);

        List<UserDto> result =  userService.getAll();

        for(int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i).getId(), result.get(i).getId());
            assertEquals(users.get(i).getFirstName(), result.get(i).getFirstName());
            assertEquals(users.get(i).getLastName(), result.get(i).getLastName());
            assertEquals(users.get(i).getEmail(), result.get(i).getEmail());
        }

    }

    @Test
    public void findById_customUser_shouldBeEqualExpected() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withEmail("user@email.com")
                .withFirstName("Lokesh")
                .withLastName("Gupta")
                .build();
        when(userRepository.findById(1L)).thenReturn((Optional.of(user)));

        UserDto userDto = userService.findById(1L);

        assertEquals("Lokesh", userDto.getFirstName());
        assertEquals("Gupta", userDto.getLastName());
        assertEquals("user@email.com", userDto.getEmail());
    }

    @Test
    public void delete_repositoryShouldBeCallOnce() {
        userService.delete(1L);

       verify(userRepository, times(1)).deleteById(1L);
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void isEmailFree_customUserWithEmailExist_shouldThrowEmailExistException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withEmail("user2@email.com")
                .withFirstName("Lokesh")
                .withLastName("Gupta")
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        userService.isEmailFree(user.getEmail());
    }

    @Test
    public void isEmailFree_customUserWithOtherEmailExist_shouldReturnTrue() {
        when(userRepository.findByEmail("Alexei@gmail.com")).thenReturn(Optional.empty());

        boolean result = userService.isEmailFree("Alexei@gmail.com");
        assertTrue(result);
    }

    @Test(expected = ItemNotFoundException.class)
    public void changeUserPasswordById_userNotExist_shouldThrowItemNotFoundException() {
        userService.changeUserPasswordById(1L, "1234", "12345");
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordTheSame_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                    .withId(1L)
                    .withHashedPassword("1234")
                    .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "1234", "1234")
        );

        assertTrue(exception.getMessage().contains("Password must not be the same as old!"));
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordInvalid_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "1234", "123")
        );

        assertTrue(exception.getMessage().contains("Password doesn't exist!"));
    }

    @Test
    public void changeUserPasswordById_customUserExist_shouldReturnString() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        String result = userService.changeUserPasswordById(1L, "12345", "1234");

        assertTrue(result.contains("Password has been successfully changed!"));
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordContainsIllegalCharacters_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "12345%", "1234")
        );

        assertTrue(exception.getMessage().contains("[Password must not have illegal characters]"));
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordContainsWhiteSpace_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "12345 ", "1234")
        );

        assertTrue(exception.getMessage().contains("[Password must not contain any whitespace," +
                " Password must not have illegal characters]"));
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordTooShort_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "12", "1234")
        );

        assertTrue(exception.getMessage().contains("[Password must not be shorter than 4 characters]"));
    }

    @Test
    public void changeUserPasswordById_customUserExistAndPasswordTooLong_shouldThrowValidationException() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withHashedPassword("1234")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(ValidationException.class, ()->
            userService.changeUserPasswordById(1L, "1212121212112312323123231312312312312312312321312",
                    "1234")
        );

        assertTrue(exception.getMessage().contains("[Password must not be longer than 32 characters]"));
    }

    @Test
    public void register_customRoleAndCustomUserRegisterDataExist_shouldBeEqualExpected() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        when(roleRepository.findByName(role.getName())).thenReturn(role);
        when(userRepository.save(any())).thenReturn(new User());

        UserRegisterData userRegisterData = new UserRegisterData();
        userRegisterData.setEmail("Alexei@gmail.com");
        userRegisterData.setFirstName("Alexei");
        userRegisterData.setLastName("Solo");
        userRegisterData.setPassword("12345678");
        userService.register(userRegisterData);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void logout_customUserExist_serviceShouldBeCallOnce() {
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withEmail("Alexei@gmail.com")
                .build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", user.getEmail());
        hashMap.put("id", user.getId().toString());
        userService.logout(hashMap);

        verify(jwtBlacklistService, times(1)).saveTokenToBlacklist(any(JwtBlacklist.class));
    }

    @Test
    public void logout_userNotExist_shouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", "Alexei@gmail.com");
        hashMap.put("id", "1");
        Exception exception = assertThrows(UserNotFoundException.class, ()->
            userService.logout(hashMap)
        );

        assertTrue(exception.getMessage().contains("No such user in database"));
    }

    @Test
    public void login_customUserExistAndStatusIsActive_shouldThrowJwtAuthenticationException() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setEmail("Alexei@gmail.com");
        requestDto.setPassword("12345678");
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withStatus(Status.ACTIVE)
                .build();
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(JwtAuthenticationException.class, ()->
            userService.login(requestDto)
        );

        assertTrue(exception.getMessage().contains("User is already logged in"));
    }

    @Test
    public void login_customUserExistAndStatusIsBlocked_shouldThrowJwtAuthenticationException() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setEmail("Alexei@gmail.com");
        requestDto.setPassword("12345678");
        User user = new UsersGenerator.Builder()
                .withId(1L)
                .withStatus(Status.BLOCKED)
                .build();
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(JwtAuthenticationException.class, ()->
            userService.login(requestDto)
        );

        assertTrue(exception.getMessage().contains("User is blocked"));
    }

    @Test
    public void login_customUserExist_shouldThrowUserNotFoundException() {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setEmail("Alexei@gmail.com");
        requestDto.setPassword("12345678");
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, ()->
            userService.login(requestDto)
        );

        assertTrue(exception.getMessage().contains("User not found"));
    }
}
