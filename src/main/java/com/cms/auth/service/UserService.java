package com.cms.auth.service;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.UserResponse;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(UUID id);
}