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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        request.setUsername("Moses");
        request.setAddress("sabo yaba");

        when(userRepository.existsByEmail("moses@test.com"))
                .thenReturn(false);

        User savedUser = new User();
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword(request.getPassword());
        savedUser.setFirstName(request.getFirstName());
        savedUser.setLastName(request.getLastName());
        savedUser.setUsername(request.getUsername());

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response = userService.register(request);

        assertNotNull(response);
        assertEquals("Moses", response.getUsername());
        assertEquals("Registration successful", response.getMessage());

        verify(userRepository).existsByEmail("moses@test.com");
        verify(userRepository).save(any(User.class));
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

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnUserResponse() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("password123");
        request.setUsername("Moses");

        User user = new User();
        user.setEmail("moses@test.com");
        user.setPassword("password123");
        user.setUsername("Moses");

        when(userRepository.findByEmail("moses@test.com"))
                .thenReturn(Optional.of(user));

        UserResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("Moses", response.getUsername());
        assertEquals("Logged in successfully", response.getMessage());

        verify(userRepository).findByEmail("moses@test.com");


    }
    @Test
    void login_withWrongPassword_shouldReturnErrorMessage() {

        UserRequest request = new UserRequest();
        request.setEmail("moses@test.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setEmail("moses@test.com");
        user.setPassword("password123");

        when(userRepository.findByEmail("moses@test.com"))
                .thenReturn(Optional.of(user));


        UserResponse response = userService.login(request);


        assertNotNull(response);
        assertNull(response.getUsername());
        assertEquals("Invalid password", response.getMessage());
    }

    @Test
    void registerUser_withNullRequest_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(null));
    }
}