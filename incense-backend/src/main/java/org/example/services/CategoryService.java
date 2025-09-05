
// src/main/java/com/ecommerce/sellerapp/service/CategoryService.java
package org.example.services;

import org.example.dto.CategoryDto;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = convertToEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }
    
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        
        category.setLabel(categoryDto.getLabel());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImage(categoryDto.getImage());
        category.setComments(categoryDto.getComments());
        
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory);
    }
    
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
    
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setLabel(category.getLabel());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setImage(category.getImage());
        dto.setComments(category.getComments());
        return dto;
    }
    
    private Category convertToEntity(CategoryDto dto) {
        Category category = new Category();
        category.setLabel(dto.getLabel());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImage(dto.getImage());
        category.setComments(dto.getComments());
        return category;
    }
}