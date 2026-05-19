package com.cms.admin.repository;

import com.cms.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    boolean existsByEmployeeId(String employeeId);

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Admin> findByEmployeeId(String employeeId);

    Page<Admin> findAllBy(
            Pageable pageable
    );

    Page<Admin> findByUserId(
            UUID userId,
            Pageable pageable
    );
}