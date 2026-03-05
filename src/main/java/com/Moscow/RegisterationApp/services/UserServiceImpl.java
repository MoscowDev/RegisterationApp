package com.Moscow.RegisterationApp.services;

import com.Moscow.RegisterationApp.data.models.User;
import com.Moscow.RegisterationApp.dtos.request.UserRequest;
import com.Moscow.RegisterationApp.dtos.response.UserResponse;
import com.Moscow.RegisterationApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(UserRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setUsername(savedUser.getUsername());
        response.setMessage("Registration successful");

        return response;
    }

    @Override
    public UserResponse login(UserRequest request) {

        if (request == null || request.getEmail() == null || request.getEmail().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            UserResponse errorResponse = new UserResponse();
            errorResponse.setUsername(null);
            errorResponse.setMessage("Email and password are required");
            return errorResponse;
        }

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            UserResponse errorResponse = new UserResponse();
            errorResponse.setUsername(null);
            errorResponse.setMessage("User not found");
            return errorResponse;
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(request.getPassword())) {
            UserResponse errorResponse = new UserResponse();
            errorResponse.setUsername(null);
            errorResponse.setMessage("Invalid password");
            return errorResponse;
        }

        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setMessage("Logged in successfully");
        return response;
    }
}