package lumi.insert.app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.entity.Category;

public class CategoryServiceCreateTest extends BaseCategoryServiceTest{

    @Test
    public void testCreateCategoryValid(){
        when(categoryRepositoryMock.existsByName("Electronics")).thenReturn(false);

        CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
        .name("Electronics")
        .build();

        Category mockCategoryQuery = Category.builder()
        .name("Electronics")
        .build();

        Category mockCategoryResult = Category.builder()
        .id(1L)
        .name("Electronics")
        .build();

        mockCategoryResult.setCreatedAt(LocalDateTime.now());
        mockCategoryResult.setUpdatedAt(LocalDateTime.now());

        when(categoryRepositoryMock.save(mockCategoryQuery)).thenReturn(mockCategoryResult);

        CategoryResponse categoryResponse = new CategoryResponse(mockCategoryResult.getId(), mockCategoryResult.getName(), null, null,LocalDateTime.now(), LocalDateTime.now());
        when(categoryMapper.createDtoResponseFromCategory(mockCategoryResult)).thenReturn(categoryResponse);

        CategoryResponse mockCategoryResponse = categoryServiceMock.createCategory(categoryCreateRequest);
        assertNotNull(mockCategoryResponse);
        assertEquals(1L, mockCategoryResponse.id());
        assertEquals("Electronics", mockCategoryResponse.name());
        assertNotNull(mockCategoryResponse.createdAt());
        assertNotNull(mockCategoryResponse.updatedAt());
    }

    @Test
    public void testCreateCategoryWithExistingName(){
        when(categoryRepositoryMock.existsByName("Shoes")).thenReturn(true);

        CategoryCreateRequest categoryCreateRequest = CategoryCreateRequest.builder()
        .name("Shoes")
        .build();

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.createCategory(categoryCreateRequest));
    }
}
