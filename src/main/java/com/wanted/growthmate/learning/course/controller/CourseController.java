package com.wanted.growthmate.learning.course.controller;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.enrollment.service.EnrollmentService;
import com.wanted.growthmate.learning.course.domain.dto.CourseCreateRequest;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.dto.CourseEditRequest;
import com.wanted.growthmate.learning.course.domain.dto.InstructorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/courses")
    public String getCourses(Model model) {
        List<CourseDetailResponse> courses = courseService.getCourses();
        List<CategoryResponse> categories = courseService.getAllCategories();
        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
        return "course-list";
    }

    // 강좌 상세
    @GetMapping("/courses/{id}")
    public String getCourseDetails(@PathVariable long id, Model model) {
        //수강 progress
        return "course-detail";
    }

    @GetMapping("/instructor/courses")
    public String instructorCourses(Model model) {
        List<InstructorCourseSummaryResponse> instructorCourses = courseService.getInstructorCourses();
        model.addAttribute("courses", instructorCourses);
        return "instructor-course-list";
    }

    @GetMapping("/instructor/courses/new")
    public String newInstructorCourse(Model model) {
        List<CategoryResponse> categories = courseService.getAllCategories();
        model.addAttribute("form", new CourseCreateRequest());
        model.addAttribute("categories", categories);
        return "course-new";
    }

    @PostMapping("/instructor/courses")
    public String createInstructorCourse(@Valid @ModelAttribute("form") CourseCreateRequest request,
                                         BindingResult bindingResult,
                                         @RequestParam String action,
                                         Model model) {
        /*if (bindingResult.hasErrors()) {
            // 다시 카테고리 목록 채워서 폼으로 회귀
            List<CategoryResponse> categories = courseService.getAllCategories();
            model.addAttribute("categories", categories);

            return "course-new"; // 같은 템플릿 다시 보여줌
        }*/
        courseService.createCourse(
                action,
                1L,
                request.getCategoryId(),
                request.getTitle(),
                request.getDescription(),
                request.getImageUrl(),
                request.getPointAmount()
        );
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        //수정 폼에 뿌릴 데이터 (이전 값 미리 채우기)
        CourseEditRequest courseEditForm = courseService.getCourseEditForm(id);
        //카테고리 목록 (select 옵션용)
        List<CategoryResponse> categories = courseService.getAllCategories();

        model.addAttribute("form", courseEditForm);
        model.addAttribute("categories", categories);
        return "course-new";
    }

    @PostMapping("/instructor/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/instructor/courses";
    }
}
