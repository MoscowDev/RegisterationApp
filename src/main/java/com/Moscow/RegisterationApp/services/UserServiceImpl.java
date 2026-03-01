package com.Moscow.RegisterationApp.services;

import com.Moscow.RegisterationApp.data.models.User;
import com.Moscow.RegisterationApp.dtos.request.UserRequest;
import com.Moscow.RegisterationApp.dtos.response.UserResponse;
import com.Moscow.RegisterationApp.repositories.UserRepository;
import org.springframework.stereotype.Service;

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

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());

        User savedUser = userRepository.save(user);

        // Response
        UserResponse response = new UserResponse();
        response.setUsername(savedUser.getUsername());
        response.setMessage("Registration successful");

        return response;
    }

    @Override
    public UserResponse login(UserRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalStateException("Invalid password");
        }

        UserResponse response = new UserResponse();
        response.setUsername(user.getUsername());
        response.setMessage("logged in successfully");

        return response;
    }
}