package com.cms.admin.repository;

import com.cms.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    boolean existsByEmployeeId(String employeeId);

    boolean existsByPersonalEmail(String personalEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Admin> findByEmployeeId(String employeeId);
}