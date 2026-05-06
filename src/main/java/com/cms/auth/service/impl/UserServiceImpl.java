package com.cms.auth.service.impl;

import com.cms.auth.dto.CreateUserRequest;
import com.cms.auth.dto.UserResponse;
import com.cms.auth.entity.User;
import com.cms.auth.repository.UserRepository;
import com.cms.auth.service.UserService;
import com.cms.common.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setStatus(UserStatus.ACTIVE);

        // 🔐 HASH PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);

        return map(saved);
    }

    @Override
    public UserResponse getUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return map(user);
    }

    private UserResponse map(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setRole(user.getRole());
        res.setStatus(user.getStatus());
        return res;
    }
}