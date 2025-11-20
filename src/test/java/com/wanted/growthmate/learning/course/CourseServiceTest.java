package com.wanted.growthmate.learning.course;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.domain.dto.CourseCreateRequest;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.model.CourseEdit;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import com.wanted.growthmate.learning.course.exception.CourseNotFound;
import com.wanted.growthmate.learning.course.repository.CourseRepository;
import com.wanted.growthmate.learning.course.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//`@Transactional
class CourseServiceTest {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;

    @Test
    void 강좌_생성() throws Exception {
        CourseCreateRequest dto = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();

        CourseDetailResponse savedCourse = courseService.createCourse(CourseState.DRAFT.name()
                , dto.getUserId(), dto.getCategoryId(), dto.getTitle(), dto.getDescription(), dto.getImageUrl(), dto.getPointAmount());
        assertThat(savedCourse.getTitle()).isEqualTo("강좌 제목");
    }

    @Test
    void 강좌_단일_조회() throws Exception {
        CourseCreateRequest dto = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();

        courseService.createCourse(CourseState.DRAFT.name(), dto.getUserId(), dto.getCategoryId(), dto.getTitle(), dto.getDescription(), dto.getImageUrl(), dto.getPointAmount());

        Optional<Course> findCourse = courseService.getCourse(1L);
        assertThat(findCourse).isPresent();
        Course course = findCourse.get();
        assertThat(course.getTitle()).isEqualTo("강좌 제목");
        assertThat(course.getDescription()).isEqualTo("강좌 설명");
    }

    @Test
    void 강좌_리스트_조회() throws Exception {
        CourseCreateRequest dto1 = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();

        courseService.createCourse(CourseState.DRAFT.name(), dto1.getUserId(), dto1.getCategoryId(), dto1.getTitle(), dto1.getDescription(), dto1.getImageUrl(), dto1.getPointAmount());

        CourseCreateRequest dto2 = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목2")
                .description("강좌 설명2")
                .imageUrl("이미지url2")
                .pointAmount(200L)// 200P
                .build();

        courseService.createCourse(CourseState.DRAFT.name(), dto2.getUserId(), dto2.getCategoryId(), dto2.getTitle(), dto2.getDescription(), dto2.getImageUrl(), dto2.getPointAmount());

        List<CourseDetailResponse> savedCourses = courseService.getCourses();
        assertThat(savedCourses).isNotEmpty();
        assertThat(savedCourses).hasSize(savedCourses.size()); //요소 길이
    }

    @Test
    void 강좌_수정() throws Exception {
        CourseCreateRequest dto = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();

        courseService.createCourse(CourseState.DRAFT.name(), dto.getUserId(), dto.getCategoryId(), dto.getTitle(), dto.getDescription(), dto.getImageUrl(), dto.getPointAmount());

        CourseEdit courseEdit = CourseEdit.builder()
                .title("강좌 수정")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();

        CourseDetailResponse updatedCourse = courseService.update(1L, courseEdit);
        org.junit.jupiter.api.Assertions.assertEquals(updatedCourse.getTitle(), "강좌 수정");
    }

    @Test
    void 강좌_삭제() throws Exception {
        CourseCreateRequest dto = CourseCreateRequest.builder()
                .userId(1L)// 강사ID
                .categoryId(1L)
                .title("강좌 제목")
                .description("강좌 설명")
                .imageUrl("이미지url")
                .pointAmount(200L)// 200P
                .build();
        CourseDetailResponse course = courseService.createCourse(CourseState.DRAFT.name(), dto.getUserId(), dto.getCategoryId(), dto.getTitle(), dto.getDescription(), dto.getImageUrl(), dto.getPointAmount());

        courseService.deleteCourse(course.getId());

        Course findCourse = courseRepository.findById(course.getId())
                .orElseThrow(() -> new CourseNotFound("Course not found with id: " + course.getId()));
        assertThat(findCourse.isDeleted()).isTrue();
    }

    @Test
    void 카테고리_조회() {
        List<CategoryResponse> allCategories = courseService.getAllCategories();
        assertThat(allCategories).isNotEmpty();

        assertThat(allCategories)
                .extracting("categoryName")
                .contains("프로그래밍", "웹 개발", "데이터베이스", "디자인");
    }
}