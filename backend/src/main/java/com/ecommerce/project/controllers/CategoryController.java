package com.ecommerce.project.controllers;

import com.ecommerce.project.config.AppConstants;
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
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
                                                                             ) {
        CategoryResponseDTO categories = categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);
        ApiResponse<CategoryResponseDTO> response = new ApiResponse<>("Categories retrieved successfully", categories);
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
        ApiResponse<String> response = new ApiResponse<>(message, null);
        return ResponseEntity.ok(response);
    }

}

