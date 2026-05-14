package com.cms.admin.entity;

import com.cms.auth.entity.User;
import com.cms.common.entity.BaseEntity;
import com.cms.common.enums.Gender;
import com.cms.common.enums.BloodGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDate;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 20)
    private BloodGroup bloodGroup;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "profile_image_url", length = 1000)
    private String profileImageUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "research_papers_url", length = 1000)
    private String researchPapersUrl;

    @Column(name = "personal_email", unique = true, length = 100)
    private String personalEmail;

    @Column(name = "current_address", length = 2000)
    private String currentAddress;

    @Column(name = "permanent_address", length = 2000)
    private String permanentAddress;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(nullable = false)
    private String designation;

    @Column(name = "joining_date")
    private LocalDate joiningDate;
}