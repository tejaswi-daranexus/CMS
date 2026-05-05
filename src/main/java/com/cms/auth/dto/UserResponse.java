package com.cms.auth.dto;

import com.cms.common.enums.Role;
import com.cms.common.enums.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {

    private UUID id;
    private String email;
    private Role role;
    private UserStatus status;

}