package com.wanted.growthmate.category.dto;

import com.wanted.growthmate.category.domain.entity.Category;

public class CategoryResponse {
    private Long id; //선택을 위해 필요함
    private String categoryName;

    public CategoryResponse(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
    }

    public static CategoryResponse from (Category category) {
        return new CategoryResponse(category);
    }

    public Long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
