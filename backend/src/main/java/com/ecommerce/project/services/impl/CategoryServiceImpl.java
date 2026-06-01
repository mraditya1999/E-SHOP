package com.ecommerce.project.services.impl;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceAlreadyExist;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.requestDto.CategoryDTO;
import com.ecommerce.project.payload.responseDto.CategoryResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private CategoryDTO convertToDto(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    private Category convertToEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }

    @Override
    public CategoryResponseDTO getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        if (categoryPage.isEmpty()) {
            throw new APIException("No categories found");
        }

        Page<CategoryDTO> categoryDTOPage = categoryPage.map(this::convertToDto);
        return new CategoryResponseDTO(
                categoryDTOPage.getContent(),
                categoryDTOPage.getNumber(),
                categoryDTOPage.getSize(),
                categoryDTOPage.getTotalElements(),
                categoryDTOPage.getTotalPages(),
                categoryDTOPage.isLast()
        );
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (existingCategory != null) {
            throw new ResourceAlreadyExist("Category", "categoryName", categoryDTO.getCategoryName());
        }
        Category category = convertToEntity(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public CategoryDTO updateCategoryById(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        Category duplicateCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (duplicateCategory != null && !duplicateCategory.getCategoryId().equals(categoryId)) {
            throw new ResourceAlreadyExist("Category", "categoryName", categoryDTO.getCategoryName());
        }

        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return convertToDto(updatedCategory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        categoryRepository.delete(category);
        return "Category with ID " + categoryId + " deleted successfully";
    }
}