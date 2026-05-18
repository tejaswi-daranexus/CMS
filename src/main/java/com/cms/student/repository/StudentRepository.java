package com.cms.student.repository;

import com.cms.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    boolean existsByRollNumber(String rollNumber);

    boolean existsByPersonalEmail(String personalEmail);

    Optional<Student> findByRollNumber(String rollNumber);

    Page<Student> findAllBy(Pageable pageable);


    // authorization-aware repository queries
    @Query("""
    SELECT s
    FROM Student s
    WHERE s.section.branch.department.id = :departmentId
""")
    Page<Student> findAllByDepartmentId(
            @Param("departmentId") UUID departmentId,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT s
    FROM Student s
    WHERE s.section.id IN :sectionIds
""")
    Page<Student> findAllBySectionIds(
            @Param("sectionIds") List<UUID> sectionIds,
            Pageable pageable
    );
}