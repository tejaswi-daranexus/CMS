package com.cms.common.security;

import com.cms.common.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static UUID getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return UUID.fromString(authentication.getName());
    }

    public static Role getCurrentUserRole() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority ->
                        Role.valueOf(
                                authority.getAuthority().replace("ROLE_", "")
                        )
                )
                .orElseThrow(() ->
                        new RuntimeException("No role found"));
    }
}