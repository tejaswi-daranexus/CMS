package com.cms.student.entity;

import com.cms.auth.entity.User;
import com.cms.common.entity.BaseEntity;
import com.cms.section.entity.Section;
import com.cms.common.enums.Gender;
import com.cms.common.enums.Category;
import com.cms.common.enums.BloodGroup;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "students")
@Getter
@Setter
public class Student extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "roll_number", nullable = false, unique = true)
    private String rollNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "profile_image_url", length = 1000)
    private String profileImageUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "personal_email", unique = true, length = 100)
    private String personalEmail;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "guardian_name", length = 100)
    private String guardianName;

    @Column(name = "guardian_relation", length = 50)
    private String guardianRelation;

    @Column(name = "guardian_phone_number", length = 20)
    private String guardianPhoneNumber;

    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "father_phone_number", length = 20)
    private String fatherPhoneNumber;

    @Column(name = "mother_name", length = 100)
    private String motherName;

    @Column(name = "mother_phone_number", length = 20)
    private String motherPhoneNumber;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 20)
    private BloodGroup bloodGroup;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Category category;

    @Column(name = "current_address", length = 2000)
    private String currentAddress;

    @Column(name = "permanent_address", length = 2000)
    private String permanentAddress;

    @Column(name = "admission_year")
    private Integer admissionYear;

    @Column(name = "is_hosteller", nullable = false)
    private boolean hosteller = false;

    @Column(name = "profile_completed", nullable = false)
    private boolean profileCompleted = false;
}