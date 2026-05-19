package com.cms.student.service;

import com.cms.common.dto.BranchMiniResponse;
import com.cms.common.dto.DepartmentMiniResponse;
import com.cms.common.dto.SectionMiniResponse;
import com.cms.common.security.SecurityUtil;
import com.cms.student.dto.StudentDetailResponse;
import com.cms.student.dto.StudentSummaryResponse;
import com.cms.student.entity.Student;
import com.cms.student.repository.StudentRepository;

import com.cms.admin.entity.Admin;
import com.cms.admin.repository.AdminRepository;
import com.cms.common.enums.Role;
import com.cms.faculty.entity.Faculty;
import com.cms.faculty.repository.FacultyRepository;
import com.cms.sessionsubject.entity.SessionSubject;
import com.cms.sessionsubject.repository.SessionSubjectRepository;

import com.cms.common.exception.ForbiddenException;
import com.cms.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentQueryServiceImpl implements StudentQueryService {

    private final StudentRepository studentRepository;

    private final AdminRepository adminRepository;

    private final FacultyRepository facultyRepository;

    private final SessionSubjectRepository sessionSubjectRepository;

    @Override
    public StudentDetailResponse getMyProfile() {

        Student student = studentRepository.findById(
                        SecurityUtil.getCurrentUserId()
                )
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return mapToStudentDetailResponse(student);
    }


    //SUPER_ADMIN: Gets all students
    //ADMIN: Gets department-scoped students
    //FACULTY: Gets students in assigned sections
    //STUDENT: Blocked entirely.
    @Override
    @Transactional(readOnly = true)
    public Page<StudentSummaryResponse> getStudents(
            int page,
            int size
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<Student> students;

        switch (role) {

            case SUPER_ADMIN -> {

                students = studentRepository.findAllBy(pageable);
            }

            case ADMIN -> {

                Admin admin = adminRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new RuntimeException("Admin not found"));

                UUID departmentId = admin.getDepartment().getId();

                students = studentRepository.findAllByDepartmentId(
                        departmentId,
                        pageable
                );
            }

            case FACULTY -> {

                Faculty faculty = facultyRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new RuntimeException("Faculty not found"));

                List<UUID> sectionIds = sessionSubjectRepository
                        .findByFaculty_UserId(faculty.getUserId())
                        .stream()
                        .map(sessionSubject ->
                                sessionSubject.getSection().getId()
                        )
                        .distinct()
                        .toList();

                students = studentRepository.findAllBySectionIds(
                        sectionIds,
                        pageable
                );
            }

            default -> throw new RuntimeException(
                    "You are not allowed to access students list"
            );
        }

        return students.map(student ->
                StudentSummaryResponse.builder()

                        .userId(student.getUser().getId())

                        .username(student.getUser().getUsername())

                        .institutionalEmail(
                                student.getUser().getEmail()
                        )

                        .fullName(student.getFullName())

                        .rollNumber(student.getRollNumber())

                        .phoneNumber(student.getPhoneNumber())

                        .sectionName(
                                student.getSection().getName()
                        )

                        .branchName(
                                student.getSection()
                                        .getBranch()
                                        .getName()
                        )

                        .departmentName(
                                student.getSection()
                                        .getBranch()
                                        .getDepartment()
                                        .getName()
                        )

                        .admissionYear(student.getAdmissionYear())

                        .status(student.getUser().getStatus())

                        .build()
        );
    }


    @Override
    @Transactional(readOnly = true)
    public StudentDetailResponse getStudentById(
            UUID studentId
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found"
                        ));

        validateStudentAccess(student);

        return mapToStudentDetailResponse(student);
    }

    private void validateStudentAccess(
            Student student
    ) {

        Role role = SecurityUtil.getCurrentUserRole();

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        switch (role) {

            case SUPER_ADMIN -> {

                return;
            }

            case ADMIN -> {

                Admin admin = adminRepository.findById(currentUserId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Admin not found"
                                ));

                UUID adminDepartmentId = admin.getDepartment()
                        .getId();

                UUID studentDepartmentId = student.getSection()
                        .getBranch()
                        .getDepartment()
                        .getId();

                if (!adminDepartmentId.equals(studentDepartmentId)) {

                    throw new ForbiddenException(
                            "You cannot access this student"
                    );
                }
            }

            case FACULTY -> {

                List<UUID> sectionIds = sessionSubjectRepository
                        .findByFaculty_UserId(currentUserId)
                        .stream()
                        .map(sessionSubject ->
                                sessionSubject.getSection().getId()
                        )
                        .distinct()
                        .toList();

                UUID studentSectionId = student.getSection()
                        .getId();

                if (!sectionIds.contains(studentSectionId)) {

                    throw new ForbiddenException(
                            "You cannot access this student"
                    );
                }
            }

            case STUDENT -> {

                if (!student.getUserId().equals(currentUserId)) {

                    throw new ForbiddenException(
                            "You cannot access this student"
                    );
                }
            }

            default -> throw new ForbiddenException(
                    "Access denied"
            );
        }
    }


    private StudentDetailResponse mapToStudentDetailResponse(
            Student student
    ) {

        return StudentDetailResponse.builder()

                .userId(student.getUser().getId())

                .username(student.getUser().getUsername())

                .institutionalEmail(
                        student.getUser().getEmail()
                )

                .status(student.getUser().getStatus())

                .rollNumber(student.getRollNumber())

                .fullName(student.getFullName())

                .profileImageUrl(student.getProfileImageUrl())

                .linkedinUrl(student.getLinkedinUrl())

                .githubUrl(student.getGithubUrl())

                .personalEmail(student.getPersonalEmail())

                .phoneNumber(student.getPhoneNumber())

                .guardianName(student.getGuardianName())

                .guardianRelation(student.getGuardianRelation())

                .guardianPhoneNumber(
                        student.getGuardianPhoneNumber()
                )

                .fatherName(student.getFatherName())

                .fatherPhoneNumber(
                        student.getFatherPhoneNumber()
                )

                .motherName(student.getMotherName())

                .motherPhoneNumber(
                        student.getMotherPhoneNumber()
                )

                .emergencyContactName(
                        student.getEmergencyContactName()
                )

                .emergencyContactPhone(
                        student.getEmergencyContactPhone()
                )

                .dateOfBirth(student.getDateOfBirth())

                .gender(student.getGender())

                .bloodGroup(student.getBloodGroup())

                .category(student.getCategory())

                .currentAddress(student.getCurrentAddress())

                .permanentAddress(student.getPermanentAddress())

                .admissionYear(student.getAdmissionYear())

                .hosteller(student.isHosteller())

                .profileCompleted(
                        student.isProfileCompleted()
                )

                .section(
                        SectionMiniResponse.builder()

                                .id(student.getSection().getId())

                                .name(student.getSection().getName())

                                .branch(
                                        BranchMiniResponse.builder()

                                                .id(student.getSection()
                                                        .getBranch()
                                                        .getId())

                                                .name(student.getSection()
                                                        .getBranch()
                                                        .getName())

                                                .code(student.getSection()
                                                        .getBranch()
                                                        .getCode())

                                                .department(
                                                        DepartmentMiniResponse
                                                                .builder()

                                                                .id(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getId())

                                                                .name(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getName())

                                                                .code(student.getSection()
                                                                        .getBranch()
                                                                        .getDepartment()
                                                                        .getCode())

                                                                .build()
                                                )

                                                .build()
                                )

                                .build()
                )

                .build();
    }
}