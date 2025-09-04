package org.example.controller;

import org.example.dto.CategoryDto;
import org.example.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Profile("test")
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getAllCategoriesReturnsListOfCategories() {
        List<CategoryDto> categories = List.of(new CategoryDto("1", "Electronics", "", "", ""), new CategoryDto("2", "Books", "", "", ""));
        when(categoryService.getAllCategories()).thenReturn(categories);

        List<CategoryDto> result = categoryController.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
    }

    @Test
    void getCategoryByIdReturnsCategoryWhenFound() {
        CategoryDto category = new CategoryDto("1", "Electronics", "", "", "");
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<CategoryDto> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Electronics", response.getBody().getName());
    }

    @Test
    void getCategoryByIdReturnsNotFoundWhenCategoryDoesNotExist() {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        ResponseEntity<CategoryDto> response = categoryController.getCategoryById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createCategoryReturnsCreatedCategory() {
        CategoryDto category = new CategoryDto(null, "Electronics", "", "", "");
        CategoryDto createdCategory = new CategoryDto(1L,"1", "Electronics", "", "", "");
        when(categoryService.createCategory(category)).thenReturn(createdCategory);

        CategoryDto result = categoryController.createCategory(category);
        System.out.println(result);
        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());
    }

    @Test
    void updateCategoryReturnsUpdatedCategoryWhenSuccessful() {
        CategoryDto category = new CategoryDto(null, "Updated Electronics", "", "", "");
        CategoryDto updatedCategory = new CategoryDto("1", "Updated Electronics", "", "", "");
        when(categoryService.updateCategory(1L, category)).thenReturn(updatedCategory);

        ResponseEntity<CategoryDto> response = categoryController.updateCategory(1L, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Electronics", response.getBody().getName());
    }

    @Test
    void updateCategoryReturnsNotFoundWhenCategoryDoesNotExist() {
        CategoryDto category = new CategoryDto(null, "Updated Electronics", "", "", "");
        when(categoryService.updateCategory(1L, category)).thenThrow(new RuntimeException());

        ResponseEntity response = categoryController.updateCategory(1L, category);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCategoryReturnsOkWhenSuccessful() {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}