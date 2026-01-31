package lumi.insert.app.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.CategoryEditRequest;
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.entity.Category;

public class CategoryServiceEditTest extends BaseCategoryServiceTest {
    
    @Test
    public void testEditCategoryName_shouldBeChangeCategoryName(){
        Category mockCategory = Category.builder()
        .id(1L)
        .name("unChangedName")
        .build();

        Category mockCategoryEdit = Category.builder()
        .id(1L)
        .name("changedName")
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        when(categoryRepositoryMock.save(any(Category.class))).thenReturn(mockCategoryEdit);

        CategoryEditRequest categoryEditRequest = CategoryEditRequest.builder()
        .id(1L)
        .name("changedName")
        .build();
        
        CategoryResponse categoryResponse = new CategoryResponse(mockCategoryEdit.getId(), mockCategoryEdit.getName(), null, null, null);

        when(categoryMapper.createDtoResponseFromCategory(mockCategoryEdit)).thenReturn(categoryResponse);

        CategoryResponse editCategoryName = categoryServiceMock.editCategoryName(categoryEditRequest);

        assertEquals("changedName", editCategoryName.name());
        assertEquals(1L, editCategoryName.id());
    }

    @Test
    public void testEditCategoryName_shouldThrownError(){
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        CategoryEditRequest categoryEditRequest = CategoryEditRequest.builder()
        .id(1L)
        .name("changedName")
        .build();

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.editCategoryName(categoryEditRequest));
    }

    @Test
    public void testSetCategoryActive_shouldSetCategoryToActive(){
        Category mockCategory = Category.builder()
        .name("mockCategory")
        .isActive(false)
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        when(categoryRepositoryMock.save(any(Category.class)))
        .thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse response = new CategoryResponse(
        1L, "mockCategory", null, null, null);

        when(categoryMapper.createDtoResponseFromCategory(any(Category.class)))
        .thenReturn(response);

        CategoryResponse editCategoryName = categoryServiceMock.setCategoryActive(1L);

        assertEquals("mockCategory", editCategoryName.name());
    }

    @Test
    public void testSetCategoryActive_shouldThrownAnErrorNotFound(){
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.setCategoryActive(1L));
    }

    @Test
    public void testSetCategoryActive_shouldThrownAnErrorAlreadyActive(){
        Category mockCategory = Category.builder()
        .name("mockCategory")
        .isActive(true)
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.setCategoryActive(1L));
    }

    @Test
    public void testSetCategoryInactive_shouldSetCategoryToInactive(){
        Category mockCategory = Category.builder()
        .name("mockCategory")
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        when(categoryRepositoryMock.save(any(Category.class)))
        .thenAnswer(inv -> inv.getArgument(0));

        CategoryResponse response = new CategoryResponse(
        1L, "mockCategory", null, null, null);

        when(categoryMapper.createDtoResponseFromCategory(any(Category.class)))
        .thenReturn(response);

        CategoryResponse editCategoryName = categoryServiceMock.setCategoryInactive(1L);

        assertEquals("mockCategory", editCategoryName.name());
    }

    @Test
    public void testSetCategoryInactive_shouldThrownAnErrorNotFound(){
        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.setCategoryInactive(1L));
    }

    @Test
    public void testSetCategoryInactive_shouldThrownAnErrorAlreadyActive(){
        Category mockCategory = Category.builder()
        .name("mockCategory")
        .isActive(false)
        .build();

        when(categoryRepositoryMock.findById(1L)).thenReturn(Optional.of(mockCategory));

        assertThrows(IllegalArgumentException.class, () -> categoryServiceMock.setCategoryInactive(1L));
    }

}
