package com.cms.section.repository;

import com.cms.section.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
}