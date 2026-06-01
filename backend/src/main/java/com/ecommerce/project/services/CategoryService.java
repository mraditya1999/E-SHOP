package com.ecommerce.project.services;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.payload.requestDto.CategoryDTO;
import com.ecommerce.project.payload.responseDto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategoryById(Long categoryId, CategoryDTO categoryDTO);
    String deleteCategory(Long categoryId);
}

