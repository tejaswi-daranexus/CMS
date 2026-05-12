package com.cms.student.service;

import com.cms.auth.entity.User;
import com.cms.auth.repository.UserRepository;
import com.cms.common.enums.Role;
import com.cms.common.enums.UserStatus;
import com.cms.student.dto.StudentCreateRequest;
import com.cms.student.dto.StudentResponse;
import com.cms.student.dto.StudentUpdateRequest;
import com.cms.student.entity.Student;
import com.cms.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @Override
    public StudentResponse createStudent(StudentCreateRequest request) {

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole(Role.STUDENT);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setFullName(request.fullName());
        student.setPhoneE164(request.phoneE164());
        student.setParentPhone(request.parentPhone());

        studentRepository.save(student);

        return new StudentResponse(
                user.getId(),
                student.getFullName(),
                user.getEmail(),
                student.getPhoneE164()
        );
    }

    @Override
    public List<StudentResponse> getAllStudents() {

        List<Student> students = studentRepository.findByDeletedAtIsNull();

        return students.stream()
                .map(student -> new StudentResponse(
                        student.getUser().getId(),
                        student.getFullName(),
                        student.getUser().getEmail(),
                        student.getPhoneE164()
                ))
                .toList();
    }

    @Override
    public StudentResponse updateStudent(UUID studentId, StudentUpdateRequest request) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFullName(request.fullName());
        student.setPhoneE164(request.phoneE164());
        student.setParentPhone(request.parentPhone());

        studentRepository.save(student);

        return new StudentResponse(
                student.getUser().getId(),
                student.getFullName(),
                student.getUser().getEmail(),
                student.getPhoneE164()
        );
    }

    @Override
    public void deleteStudent(UUID studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setDeletedAt(LocalDateTime.now());

        studentRepository.save(student);
    }
}