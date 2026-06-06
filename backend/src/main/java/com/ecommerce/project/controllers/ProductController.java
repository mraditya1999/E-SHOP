package com.ecommerce.project.controllers;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.payload.requestDto.ProductDTO;
import com.ecommerce.project.payload.responseDto.ProductResponseDTO;
import com.ecommerce.project.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("" )
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("admin/categories/{categoryId}/product" )
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {
        ProductDTO savedProduct = productService.addProduct(categoryId, productDTO);
        ApiResponse<ProductDTO> response = new ApiResponse<>("Product created successfully", savedProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("public/products" )
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getAllProducts(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                          @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                          @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        ApiResponse<ProductResponseDTO> response = new ApiResponse<>("Products retrieved successfully", productResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("public/categories/{categoryId}/products" )
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId,
                                                                                 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                                 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                                 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                                 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        ProductResponseDTO productResponseDTO = productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        ApiResponse<ProductResponseDTO> response = new ApiResponse<>("Products retrieved successfully", productResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("public/products/keyword/{keyword}" )
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductsByName(@PathVariable String keyword,
                                                                             @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                             @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder)  {
        ProductResponseDTO productResponseDTO = productService.getProductsByName(keyword,pageNumber,pageSize,sortBy,sortOrder);
        ApiResponse<ProductResponseDTO> response = new ApiResponse<>("Products retrieved successfully", productResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("admin/products/{productId}" )
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId) {
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);
        ApiResponse<ProductDTO> response = new ApiResponse<>("Products updated successfully", updatedProductDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("products/{productId}/image" )
    public ResponseEntity<ApiResponse<ProductDTO>> updateProductImage(@PathVariable Long productId, @RequestParam("image" ) @NotNull MultipartFile image) throws IOException {
        ProductDTO updatedProductDTO = productService.updateProductImage(productId, image);
        ApiResponse<ProductDTO> response = new ApiResponse<>("Products updated successfully", updatedProductDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("admin/products/{productId}" )
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long productId) {
        String message = productService.deleteProduct(productId);
        ApiResponse<String> response = new ApiResponse<>(message, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
