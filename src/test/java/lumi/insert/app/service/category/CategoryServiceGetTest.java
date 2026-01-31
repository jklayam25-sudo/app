package lumi.insert.app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.entity.Category;

public class CategoryServiceGetTest extends BaseCategoryServiceTest{
    
    @Test
    public void testGetCategoryById_shouldReturnCategory(){
        Category mockCategory = Category.builder()
        .id(1L)
        .name("unChangedName")
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        CategoryResponse categoryResponse = new CategoryResponse(mockCategory.getId(), mockCategory.getName(), null, null, null);

        when(categoryMapper.createDtoResponseFromCategory(mockCategory)).thenReturn(categoryResponse);

        CategoryResponse categoryById = categoryServiceMock.getCategoryById(1L);

        assertEquals(mockCategory.getName(), categoryById.name());
    }

    @Test
    public void testGetCategoryById_shouldThrownAnErrorNotFound(){
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.getCategoryById(1L));
    }

    @Test
    public void testGetCategories_shouldReturnSliceOfCategoryResponse(){
        List<Category> categories = new ArrayList<>();

        for(int i = 1; i < 10; i++){
            Category category = Category.builder()
            .name("Category" + i)
            .build();

            categories.add(category);
        }
        Slice<Category> slice = new SliceImpl<Category>(categories);
        when(categoryRepositoryMock.findAllByIsActiveTrue(any(Pageable.class))).thenReturn(slice);
        when(categoryMapper.createDtoResponseFromCategory(any(Category.class))).thenAnswer(inv -> {
            Category categoryInv = inv.getArgument(0);
            CategoryResponse categoryResponse = new CategoryResponse(null, categoryInv.getName(), null, null, null);
            return categoryResponse;
        });

        PaginationRequest paginationRequest = PaginationRequest.builder()
        .page(0)
        .size(5)
        .build();

        Slice<CategoryResponse> result = categoryServiceMock.getCategories(paginationRequest);

        assertEquals(9, result.getNumberOfElements());
        assertEquals("Category9", result.getContent().getLast().name());

    }
} 