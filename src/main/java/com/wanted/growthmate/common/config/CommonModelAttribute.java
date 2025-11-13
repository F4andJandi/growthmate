package com.wanted.growthmate.common.config;

import com.wanted.growthmate.category.dto.CategoryResponse;
import com.wanted.growthmate.category.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class CommonModelAttribute {

    private final CategoryService categoryService;

    @Autowired
    public CommonModelAttribute(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<CategoryResponse> addCategories() {
        return categoryService.findAll();
    }

    @ModelAttribute("loginUserId")
    public Long addLoginUserId(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (Long) session.getAttribute("loginUserId");
    }

    @ModelAttribute("loginUserName")
    public String addLoginUserName(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("loginUserName");
    }

    @ModelAttribute("loginUserRole")
    public Object addLoginUserRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        return session.getAttribute("loginUserRole");
    }
}
