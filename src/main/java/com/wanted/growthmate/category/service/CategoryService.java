package com.wanted.growthmate.category.service;

import com.wanted.growthmate.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
}

