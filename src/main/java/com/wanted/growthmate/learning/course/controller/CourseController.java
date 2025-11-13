package com.wanted.growthmate.learning.course.controller;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.category.service.CategoryService;
import com.wanted.growthmate.enrollment.dto.EnrollmentCreateRequest;
import com.wanted.growthmate.enrollment.service.EnrollmentService;
import com.wanted.growthmate.learning.course.domain.dto.CourseCreateRequest;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.domain.dto.CourseEditRequest;
import com.wanted.growthmate.learning.course.domain.dto.InstructorCourseSummaryResponse;
import com.wanted.growthmate.learning.course.domain.entity.Course;
import com.wanted.growthmate.learning.course.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final CategoryService categoryService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService, CategoryService categoryService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.categoryService = categoryService;
    }

    @GetMapping("/courses")
    public String getCourses(@RequestParam(required = false) Long categoryId, Model model) {
        List<CourseDetailResponse> courses;
        if (categoryId != null) {
            courses = courseService.getCoursesByCategory(categoryId);
            CategoryResponse selectedCategory = categoryService.findAll().stream()
                    .filter(c -> c.getId().equals(categoryId))
                    .findFirst()
                    .orElse(null);
            model.addAttribute("selectedCategoryName", selectedCategory != null ? selectedCategory.getCategoryName() : null);
            model.addAttribute("selectedCategoryId", categoryId);
        } else {
            courses = courseService.getCourses();
        }
        List<CategoryResponse> categories = categoryService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("categories", categories);
        return "course/course-list";
    }

    // 강좌 상세
    @GetMapping("/courses/{id}")
    public String getCourseDetails(@PathVariable long id, Model model) {
        CourseDetailResponse course = courseService.getCourse(id);

        // 로그인 구현 전: 항상 로그인 안 된 것으로 간주
        boolean loggedIn = false;
        boolean purchased = false;

        model.addAttribute("course",  course);
        model.addAttribute("loggedIn", loggedIn);
        model.addAttribute("purchased", purchased);

        return "course/course-detail";
    }

    @GetMapping("/instructor/courses")
    public String instructorCourses(Model model) {
        List<InstructorCourseSummaryResponse> instructorCourses = courseService.getInstructorCourses();
        model.addAttribute("courses", instructorCourses);
        return "course/instructor-course-list";
    }

    @GetMapping("/instructor/courses/new")
    public String newInstructorCourse(Model model) {
        List<CategoryResponse> categories = categoryService.findAll();
        model.addAttribute("form", new CourseCreateRequest());
        model.addAttribute("categories", categories);
        return "course/course-new";
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

            return "course/course-new"; // 같은 템플릿 다시 보여줌
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
        List<CategoryResponse> categories = categoryService.findAll();

        model.addAttribute("form", courseEditForm);
        model.addAttribute("categories", categories);
        return "course/course-new";
    }

    @PostMapping("/instructor/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/instructor/courses";
    }

    // 수강 신청
    @PostMapping("/courses/{id}/enroll")
    public String enrollCourse(@PathVariable Long id, jakarta.servlet.http.HttpSession session, 
                               org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("loginUserId");
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";
        }
        
        try {
            EnrollmentCreateRequest enrollmentRequest = new EnrollmentCreateRequest(userId, id);
            enrollmentService.createEnrollment(enrollmentRequest);
            redirectAttributes.addFlashAttribute("message", "수강 신청이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "수강 신청에 실패했습니다: " + e.getMessage());
        }
        
        return "redirect:/";
    }
}
