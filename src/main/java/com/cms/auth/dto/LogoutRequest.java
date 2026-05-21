package com.cms.auth.dto;

public record LogoutRequest(
        String refreshToken
) {
}