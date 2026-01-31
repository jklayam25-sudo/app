package lumi.insert.app.service;

import org.springframework.data.domain.Slice;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.request.CategoryEditRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryCreateRequest request);

    CategoryResponse editCategoryName(CategoryEditRequest request);

    CategoryResponse setCategoryActive(Long id);

    CategoryResponse setCategoryInactive(Long id);

    CategoryResponse getCategoryById(Long id);

    Slice<CategoryResponse> getCategories(PaginationRequest request);


}
