package com.wanted.growthmate.learning.course.service;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.category.repository.CategoryRepository;
import com.wanted.growthmate.learning.course.domain.dto.CourseEditRequest;
import com.wanted.growthmate.learning.course.domain.dto.InstructorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.exception.CourseNotFound;
import com.wanted.growthmate.learning.course.repository.CourseRepository;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.model.CourseEdit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseService(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    public CourseDetailResponse createCourse(String action, Long instructorId, Long courseCategoryId, String courseTitle, String courseDescription, String courseImageUrl, Long coursePointAmount) {
        Course newCourse = Course.builder()
                .userId(instructorId)
                .categoryId(courseCategoryId)
                .title(courseTitle)
                .description(courseDescription)
                .pointAmount(coursePointAmount)
                .courseState(CourseState.valueOf(action))
                .imageUrl(courseImageUrl)
                .build();

        courseRepository.save(newCourse);
        return CourseDetailResponse.from(newCourse);
    }

    public Optional<Course> getCourse(Long courseId) {
        return courseRepository.findById(courseId);
    }

    public List<CourseDetailResponse> getCourses() {
        return courseRepository.findAll().stream()
                .map(CourseDetailResponse::from)
                .toList();
    }

    public CourseDetailResponse update(Long courseId, CourseEdit courseEdit) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFound("Course not found with id: " + courseId));

        course.editCourse(courseEdit);

        courseRepository.save(course);
        return CourseDetailResponse.from(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course findCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFound("Course not found with id: " + courseId));
        findCourse.markDeleted();

        System.out.println(findCourse);
    }



    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public List<InstructorCourseSummaryResponse> getInstructorCourses() {
        return courseRepository.findAll().stream()
                .map(InstructorCourseSummaryResponse::from)
                .toList();
    }

    public CourseEditRequest getCourseEditForm(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFound("Course not found with id: " + courseId));
        return CourseEditRequest.from(course);
    }
}