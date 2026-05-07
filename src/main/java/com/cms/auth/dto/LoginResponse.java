package com.cms.auth.dto;

import com.cms.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private Role role;
}