package com.cms.sessionsubject.repository;

import com.cms.sessionsubject.entity.SessionSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SessionSubjectRepository
        extends JpaRepository<SessionSubject, UUID> {

    List<SessionSubject> findByFaculty_UserId(UUID facultyId);
}