package org.example.services;

import org.example.dto.CategoryDto;
import org.example.entity.Category;
import org.example.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@Profile("test")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void getAllCategoriesReturnsMappedCategoryDtos() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Electronics");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Books");

        Mockito.when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }

/*
    @Test
    void getCategoryByIdReturnsCategoryDtoWhenFound() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<CategoryDto> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals("Electronics", result.get().getName());
    }

    @Test
    void getCategoryByIdReturnsEmptyWhenNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CategoryDto> result = categoryService.getCategoryById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void createCategorySavesAndReturnsCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Electronics");
        Category category = new Category();
        category.setName("Electronics");
        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryDto result = categoryService.createCategory(dto);

        assertEquals(1L, result.getId());
        assertEquals("Electronics", result.getName());
    }

    @Test
    void updateCategoryUpdatesAndReturnsCategoryDtoWhenFound() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Updated");
        dto.setLabel("Label");
        dto.setDescription("Desc");
        dto.setImage("img");
        dto.setComments("comm");
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryDto result = categoryService.updateCategory(1L, dto);

        assertEquals("Updated", result.getName());
        assertEquals("Label", result.getLabel());
        assertEquals("Desc", result.getDescription());
        assertEquals("img", result.getImage());
        assertEquals("comm", result.getComments());
    }

    @Test
    void updateCategoryThrowsExceptionWhenCategoryNotFound() {
        CategoryDto dto = new CategoryDto();
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, dto));
    }

    @Test
    void deleteCategoryDeletesById() {
        categoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }*/
}