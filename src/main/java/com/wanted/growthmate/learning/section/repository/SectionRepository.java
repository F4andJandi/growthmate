package com.wanted.growthmate.learning.section.repository;

import com.wanted.growthmate.learning.section.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
    List<Section> findByCourseIdOrderByDisplayOrderAsc(Long courseId);
}

