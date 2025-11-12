package com.wanted.growthmate.learning.course.repository;

import com.wanted.growthmate.learning.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
