package com.ecommerce.project.controllers;

import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.entity.Category;
import com.ecommerce.project.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ApiResponse<List<Category>> response = new ApiResponse<>("Categories retrieved successfully", categories);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);
        ApiResponse<Category> response = new ApiResponse<>("Category created successfully", createdCategory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> updateCategoryById(@Valid @RequestBody Category category, @PathVariable Long categoryId) {
        Category updatedCategory = categoryService.updateCategoryById(categoryId, category);
        ApiResponse<Category> response = new ApiResponse<>("Category updated successfully", updatedCategory);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategoryById(@PathVariable Long categoryId) {
        String message = categoryService.deleteCategory(categoryId);
        ApiResponse<String> response = new ApiResponse<>(message, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

