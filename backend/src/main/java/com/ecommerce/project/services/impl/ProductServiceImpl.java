package com.ecommerce.project.services.impl;

import com.ecommerce.project.entity.Category;
import com.ecommerce.project.entity.Product;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.payload.requestDto.ProductDTO;
import com.ecommerce.project.payload.responseDto.ProductResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.services.FileService;
import com.ecommerce.project.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;

    @Value("${file.upload-dir}" )
    private String path;


    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper, FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    private ProductDTO convertToDto(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    private Pageable buildPageable(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc" )
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    }

    private ProductResponseDTO buildResponse(Page<Product> productPage) {
        if (productPage.isEmpty()) {
            throw new APIException("No products found" );
        }

        Page<ProductDTO> productDTOS = productPage.map(this::convertToDto);
        return new ProductResponseDTO(
                productDTOS.getContent(),
                productDTOS.getNumber(),
                productDTOS.getSize(),
                productDTOS.getTotalElements(),
                productDTOS.getTotalPages(),
                productDTOS.isLast()
        );
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));

        if (productRepository.existsByCategoryAndProductNameIgnoreCase(category, productDTO.getProductName())) {
            throw new APIException("Product with the same name already exists" );
        }

        Product product = convertToEntity(productDTO);
        double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();

        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        product.setImageUrl(null);

        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findAll(pageable);
        return buildResponse(productPage);
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", categoryId));
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageable);
        return buildResponse(productPage);
    }

    @Override
    public ProductResponseDTO getProductsByName(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Pageable pageable = buildPageable(pageNumber, pageSize, sortBy, sortOrder);
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        return buildResponse(productPage);
    }


    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setDiscount(productDTO.getDiscount());
        double specialPrice = existingProduct.getPrice() - (existingProduct.getDiscount() *
                0.01) * existingProduct.getPrice();
        existingProduct.setSpecialPrice(specialPrice);

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }


    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));

        String fileName = fileService.uploadImage(path, image);
        existingProduct.setImageUrl(fileName);
        productRepository.save(existingProduct);
        return convertToDto(existingProduct);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId));
        productRepository.delete(existingProduct);
        return "Product with ID " + productId + " deleted successfully";
    }


}
