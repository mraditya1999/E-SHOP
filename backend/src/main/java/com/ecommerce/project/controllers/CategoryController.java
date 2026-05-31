package com.ecommerce.project.controllers;

import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.entity.Category;
import com.ecommerce.project.payload.requestDto.CategoryDTO;
import com.ecommerce.project.payload.responseDto.CategoryResponseDTO;
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
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        ApiResponse<List<CategoryDTO>> response = new ApiResponse<>("Categories retrieved successfully", categories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO savedCategory = categoryService.createCategory(categoryDTO);
        ApiResponse<CategoryDTO> response = new ApiResponse<>("Category created successfully", savedCategory);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategoryById(@Valid @RequestBody CategoryDTO categoryDTO,
                                                                       @PathVariable Long categoryId) {
        CategoryDTO updatedCategory = categoryService.updateCategoryById(categoryId, categoryDTO);
        ApiResponse<CategoryDTO> response = new ApiResponse<>("Category updated successfully", updatedCategory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategoryById(@PathVariable Long categoryId) {
        String message = categoryService.deleteCategory(categoryId);
        ApiResponse<String> response = new ApiResponse<>( message,null);
        return ResponseEntity.ok(response);
    }

}

