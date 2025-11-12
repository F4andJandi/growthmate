package com.wanted.growthmate.learning.course.controller;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.learning.course.domain.dto.CourseCreateRequest;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.dto.TutorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String getCourses(Model model) {
        List<CourseDetailResponse> courses = courseService.getCourses();
        List<CategoryResponse> categories = courseService.getAllCategories();
        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
        return "course-list";
    }

    @GetMapping("/tutor/courses")
    public String tutorCourses(Model model) {
        // 로그인 정보에서 tutorId 찾고
        // tutorId 기준으로 강좌 목록 조회해서 model에 담기 course.status, course.description, course.id
        List<TutorCourseSummaryResponse> tutorCourses = courseService.getTutorCourses();
        model.addAttribute("courses", tutorCourses);
        return "tutor-course-list";
    }

    @GetMapping("/tutor/courses/new")
    public String newTutorCourse(Model model) {
        //GET으로 폼을 열 때에도 **폼-백킹 DTO(빈 값)**를 model에 넣어두면 th:object/*{...} 바인딩이 안전하게 동작하고,
        // 이후 검증 실패 시 메시지 복원(POST-Redirect-GET)도 깔끔해집니다.
        model.addAttribute("form", new CourseCreateRequest());

        List<CategoryResponse> categories = courseService.getAllCategories();
        model.addAttribute("category", categories);
        return "course-new";
    }

}
