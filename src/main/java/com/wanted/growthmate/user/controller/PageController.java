package com.wanted.growthmate.user.controller;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.category.service.CategoryService;
import com.wanted.growthmate.learning.course.domain.dto.CourseDetailResponse;
import com.wanted.growthmate.learning.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class PageController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    public PageController(CourseService courseService, CategoryService categoryService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
    }

    /**
     * 메인 페이지 - 좌측 카테고리, 가운데 강좌 카드 리스트
     * URL: http://localhost:8080/
     */
    @GetMapping("/")
    public String index(@RequestParam(required = false) Long categoryId, Model model) {
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
        return "index";
    }

    /**
     * favicon.ico 요청 처리 (404 에러 방지)
     */
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Chrome DevTools 관련 리소스 요청 처리 (404 에러 방지)
     */
    @GetMapping("/.well-known/**")
    public ResponseEntity<Void> wellKnown() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 로그인 페이지
     * URL: http://localhost:8080/login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "user/login"; // -> templates/user/login.html 파일을 찾음
    }

    /**
     * 회원가입 페이지
     * URL: http://localhost:8080/signup
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "user/signup"; // -> templates/user/signup.html 파일을 찾음
    }

    /**
     * 메인 페이지 (로그인 성공 후)
     * URL: http://localhost:8080/main
     */
    @GetMapping("/main")
    public String mainPage() {
        return "main"; // -> templates/main.html 파일을 찾음
    }
}