package com.cms.auth.repository;

import com.cms.auth.entity.User;
import com.cms.common.enums.Role;
import com.cms.common.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByRoleAndStatus(Role role, UserStatus status);

    Page<User> findAll(Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);
}