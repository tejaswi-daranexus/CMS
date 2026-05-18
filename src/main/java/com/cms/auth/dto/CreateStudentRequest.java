package com.cms.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateStudentRequest {

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UUID sectionId;

    @NotBlank
    private String rollNumber;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Integer admissionYear;
}