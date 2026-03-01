package com.Moscow.RegisterationApp.services;

import com.Moscow.RegisterationApp.dtos.request.UserRequest;
import com.Moscow.RegisterationApp.dtos.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserResponse register(UserRequest request);

    UserResponse login(UserRequest request);
}
