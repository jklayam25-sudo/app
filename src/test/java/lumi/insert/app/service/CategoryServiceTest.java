package lumi.insert.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.response.CategoryCreateResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.service.implement.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    
    @InjectMocks
    CategoryServiceImpl categoryServiceMock;

    @Mock
    CategoryRepository categoryRepositoryMock;

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

        CategoryCreateResponse mockCategoryResponse = categoryServiceMock.createCategory(categoryCreateRequest);
        assertNotNull(mockCategoryResponse);
        assertEquals(1L, mockCategoryResponse.getId());
        assertEquals("Electronics", mockCategoryResponse.getName());
        assertNotNull(mockCategoryResponse.getCreatedAt());
        assertNotNull(mockCategoryResponse.getUpdatedAt());
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
