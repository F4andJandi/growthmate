package com.wanted.growthmate.learning.course.controller;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.learning.course.domain.dto.CourseCreateRequest;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.dto.CourseEditRequest;
import com.wanted.growthmate.learning.course.domain.dto.InstructorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.domain.model.CourseState;
import com.wanted.growthmate.learning.course.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/instructor/courses")
    public String instructorCourses(Model model) {
        List<InstructorCourseSummaryResponse> instructorCourses = courseService.getInstructorCourses();
        model.addAttribute("courses", instructorCourses);
        return "instructor-course-list";
    }

    @PostMapping("/instructor/courses")
    public String createInstructorCourse(@ModelAttribute("form") CourseCreateRequest request) {
        System.out.println("### DEBUG categoryId = " + request.getCategoryId());
        courseService.createCourse(
                CourseState.DRAFT.name(),
                1L,
                request.getCategoryId(),
                request.getTitle(),
                request.getDescription(),
                request.getImageUrl(),
                request.getPointAmount()
        );
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/new")
    public String newInstructorCourse(Model model) {
        //GET으로 폼을 열 때에도 **폼-백킹 DTO(빈 값)**를 model에 넣어두면 th:object/*{...} 바인딩이 안전하게 동작하고,
        // 이후 검증 실패 시 메시지 복원(POST-Redirect-GET)도 깔끔해집니다.
        model.addAttribute("form", new CourseCreateRequest());

        List<CategoryResponse> categories = courseService.getAllCategories();
        model.addAttribute("categories", categories);
        return "course/course-new";
    }

    //이전 값 미리 채우기
    @GetMapping("/instructor/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        //수정 폼DTO를 받아야함.
        CourseEditRequest courseEditForm = courseService.getCourseEditForm(id);
        model.addAttribute("form", courseEditForm);
        return "course/course-new";
    }
}
