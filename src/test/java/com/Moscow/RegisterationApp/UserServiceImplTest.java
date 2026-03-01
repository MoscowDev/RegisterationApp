package com.Moscow.RegisterationApp;

import com.Moscow.RegisterationApp.data.models.User;
import com.Moscow.RegisterationApp.dtos.request.UserRequest;
import com.Moscow.RegisterationApp.dtos.response.UserResponse;
import com.Moscow.RegisterationApp.repositories.UserRepository;
import com.Moscow.RegisterationApp.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_shouldReturnUserResponse() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("password123");
        request.setFirstName("Moses");
        request.setLastName("Ogomegbunam");
        request.setMessage("login successful");
        request.setUsername("Moses");
        request.setAddress("sabo yaba");

        when(userRepository.existsByEmail("moses@test.com"))
                .thenReturn(false);

        // MOCK save to return a User object
        User savedUser = new User();
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword(request.getPassword());
        savedUser.setFirstName(request.getFirstName());
        savedUser.setLastName(request.getLastName());

        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("Moses", response.getUsername());
        assertEquals("Ogomegbunam", response.getMessage());
    }

    @Test
    void registerUser_withExistingEmail_shouldFail() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("moses@test.com"))
                .thenReturn(true);

        assertThrows(IllegalStateException.class,
                () -> userService.register(request));
    }

    @Test
    void login_shouldReturnUserResponse() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("password123");
        request.setFirstName("Moses");
        request.setLastName("Ogomegbunam");
        request.setMessage("login successful");
        request.setUsername("Moses");
        request.setAddress("sabo yaba");

        User user = new User();
        user.setEmail("moses@test.com");
        user.setPassword("password123");
        user.setFirstName("Moses");
        user.setLastName("Ogomegbunam");
        user.setPassword("password123");


        when(userRepository.findByEmail("moses@test.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("Moses", response.getUsername());
        assertEquals("Ogomegbunam", response.getMessage());
    }

    @Test
    void login_withWrongPassword_shouldFail() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("moses@test.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("moses@test.com"))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> userService.login(request));
    }
    @Test
    void registerUser_withNullRequest_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(null)
        );
    }
}