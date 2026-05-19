package com.cms.faculty.repository;

import com.cms.faculty.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, UUID> {

    boolean existsByEmployeeId(String employeeId);

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Faculty> findByEmployeeId(String employeeId);

    Page<Faculty> findAllBy(Pageable pageable);

    @Query("""
    SELECT f
    FROM Faculty f
    WHERE f.department.id = :departmentId
    """)
    Page<Faculty> findAllByDepartmentId(
            @Param("departmentId") UUID departmentId,
            Pageable pageable
    );

    Page<Faculty> findByUserId(
            UUID userId,
            Pageable pageable
    );
}