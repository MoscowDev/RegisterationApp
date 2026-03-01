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

        if (request == null) throw new IllegalArgumentException("User request cannot be null");
        if (request.getEmail() == null || request.getEmail().isBlank()) throw new IllegalArgumentException("Email is required");
        if (request.getPassword() == null || request.getPassword().isBlank()) throw new IllegalArgumentException("Password is required");

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        // Create user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        // Map savedUser to response
        UserResponse response = new UserResponse();
        response.setUsername(savedUser.getFirstName()); // Username = firstName
        response.setMessage(savedUser.getLastName());   // Message = lastName (as per test)


        return response;
    }

    @Override
    public UserResponse login(UserRequest request) {

        if (request == null) throw new IllegalArgumentException("User request cannot be null");
        if (request.getEmail() == null || request.getEmail().isBlank()) throw new IllegalArgumentException("Email is required");
        if (request.getPassword() == null || request.getPassword().isBlank()) throw new IllegalArgumentException("Password is required");

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalStateException("Invalid email or password");
        }

        UserResponse response = new UserResponse();
        response.setUsername(user.getFirstName());  // get from saved user
        response.setMessage(user.getLastName());    // get from saved user


        return response;
    }
}