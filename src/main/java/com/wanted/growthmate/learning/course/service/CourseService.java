package com.wanted.growthmate.learning.course.service;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.category.repository.CategoryRepository;
import com.wanted.growthmate.learning.course.domain.dto.TutorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.exception.CourseNotFound;
import com.wanted.growthmate.learning.course.repository.CourseRepository;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.model.CourseEdit;
import org.springframework.stereotype.Service;

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

    public CourseDetailResponse createCourse(Long tutorId, Long courseCategoryId, String courseTitle, String courseDescription, String courseImageUrl, Long coursePointAmount) {
        Course newCourse = Course.builder()
                .userId(tutorId)
                .categoryId(courseCategoryId)
                .title(courseTitle)
                .description(courseDescription)
                .pointAmount(coursePointAmount)
                .courseState(CourseState.DRAFT)
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

    public CourseDetailResponse update(Long course_id, CourseEdit courseEdit) {
        Course course = courseRepository.findById(course_id)
                .orElseThrow(() -> new CourseNotFound("Course not found with id: " + course_id));

        course.editCourse(courseEdit);

        courseRepository.save(course);
        return CourseDetailResponse.from(course);
    }

    public void deleteCourse(Long course_id) {
        Optional<Course> findCourse = courseRepository.findById(course_id);
        if (findCourse.isPresent()) {
            courseRepository.deleteById(course_id);
        }
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    public List<TutorCourseSummaryResponse> getTutorCourses() {
        return courseRepository.findAll().stream()
                .map(TutorCourseSummaryResponse::from)
                .toList();
    }
}
