package com.wanted.growthmate.learning.section.repository;

import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.section.domain.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourse(Course course);
    List<Section> findByCourseOrderByDisplayOrderAsc(Course course);
    List<Section> findByCourse_Id(Long courseId);
    List<Section> findByCourse_IdOrderByDisplayOrderAsc(Long courseId);
}

