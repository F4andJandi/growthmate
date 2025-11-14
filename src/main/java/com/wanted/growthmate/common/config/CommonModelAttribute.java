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
    public String addLoginUserRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object role = session.getAttribute("loginUserRole");
        if (role == null) {
            return null;
        }
        // Role enum인 경우 name()으로 변환, String인 경우 그대로 반환
        if (role instanceof Enum) {
            return ((Enum<?>) role).name();
        }
        return role.toString();
    }
}
