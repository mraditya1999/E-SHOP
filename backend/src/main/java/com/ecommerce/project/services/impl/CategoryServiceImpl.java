package com.ecommerce.project.services.impl;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceAlreadyExist;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new APIException("No categories found");
        }
            return categories;
    }

    @Override
    public Category createCategory(Category category) {
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (existingCategory != null) {
            throw new ResourceAlreadyExist("Category", "categoryName", category.getCategoryName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategoryById(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category duplicateCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if (duplicateCategory != null && !duplicateCategory.getCategoryId().equals(categoryId)) {
            throw new ResourceAlreadyExist("Category", "categoryName", category.getCategoryName());
        }
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryRepository.delete(category);
        return "Category with ID " + categoryId + " deleted successfully";
    }

}