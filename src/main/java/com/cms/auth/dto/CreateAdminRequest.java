package com.cms.auth.dto;

import com.cms.common.enums.AdminDesignation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreateAdminRequest {

    @NotNull
    private UUID departmentId;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String employeeId;

    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotNull
    private AdminDesignation designation;
}