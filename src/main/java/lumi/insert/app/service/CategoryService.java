package lumi.insert.app.service;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.response.CategoryCreateResponse;

public interface CategoryService {

    CategoryCreateResponse createCategory(CategoryCreateRequest request);

}
