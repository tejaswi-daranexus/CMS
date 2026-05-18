package com.cms.faculty.repository;

import com.cms.faculty.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FacultyRepository extends JpaRepository<Faculty, UUID> {

    boolean existsByEmployeeId(String employeeId);

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Faculty> findByEmployeeId(String employeeId);
}