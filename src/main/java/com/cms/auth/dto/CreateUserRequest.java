package com.cms.auth.dto;

import com.cms.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private Role role;

    @NotNull
    private String password;
}