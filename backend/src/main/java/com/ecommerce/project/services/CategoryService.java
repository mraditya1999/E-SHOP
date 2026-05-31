package com.ecommerce.project.services;

import com.ecommerce.project.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();

    Category createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategoryById(Long categoryId, Category category);
}

