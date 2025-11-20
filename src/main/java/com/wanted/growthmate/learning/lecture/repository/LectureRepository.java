package com.wanted.growthmate.learning.lecture.repository;

import com.wanted.growthmate.learning.lecture.domain.dto.LectureSummaryResponse;
import com.wanted.growthmate.learning.lecture.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByCourseId(Long courseId);
    List<Lecture> findBySectionId(Long sectionId);
    List<Lecture> findBySectionIdOrderByDisplayOrderAsc(Long sectionId);
}
