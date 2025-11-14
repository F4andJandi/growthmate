package com.wanted.growthmate.learning.course.repository;

import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCategoryId(Long categoryId);
    
    @Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL AND c.courseState = :state")
    List<Course> findByCourseStateAndNotDeleted(@Param("state") CourseState state);
    
    @Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL AND c.courseState = :state AND c.categoryId = :categoryId")
    List<Course> findByCategoryIdAndCourseStateAndNotDeleted(@Param("categoryId") Long categoryId, @Param("state") CourseState state);
    
    @Query("SELECT c FROM Course c WHERE c.deletedAt IS NULL")
    List<Course> findAllNotDeleted();
}
