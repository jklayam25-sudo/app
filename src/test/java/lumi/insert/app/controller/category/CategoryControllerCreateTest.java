package lumi.insert.app.controller.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import lumi.insert.app.dto.request.CategoryCreateRequest; 
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.utils.forTesting.CategoryUtils;

public class CategoryControllerCreateTest extends BaseCategoryControllerTest  {
    
    @Test
    @DisplayName("Should return created http status and DTO when all request is valid")
    public void createCategoryAPI_validRequest_returnCreatedStatusAndResponseDTO() throws Exception{
        Category mockCategory = CategoryUtils.getMockCategory();
        CategoryResponse dtoResponseFromCategory = categoryMapper.createDtoResponseFromCategory(mockCategory);

        when(categoryService.createCategory(any(CategoryCreateRequest.class))).thenReturn(dtoResponseFromCategory);

         mockMvc.perform(
            post("/api/categories")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "Category") 
        )
        .andDo(print())
        .andExpect(status().isCreated()) 
        .andExpect(jsonPath("$.errors").isEmpty())
        .andExpect(jsonPath("$.data.name").value("Category"));
    }

    @Test
    @DisplayName("Should return 400 http status when request failed the constraint")
    public void createCategoryAPI_nullParameter_return400() throws Exception{ 
         mockMvc.perform(
            post("/api/categories")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "") 
        )
        .andDo(print())
        .andExpect(status().isBadRequest()) 
        .andExpect(jsonPath("$.errors").value("Name cannot be empty"))
        .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("Should return 409 Conflict http status when category with name = request.name is same")
    public void createCategoryAPI_duplicateName_return409() throws Exception{ 
        when(categoryService.createCategory(any(CategoryCreateRequest.class))).thenThrow(new DuplicateEntityException("Category with name Category already exists"));

         mockMvc.perform(
            post("/api/categories")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "Category") 
        )
        .andDo(print())
        .andExpect(status().isConflict()) 
        .andExpect(jsonPath("$.errors").value("Category with name Category already exists"))
        .andExpect(jsonPath("$.data").isEmpty());
    }
}
