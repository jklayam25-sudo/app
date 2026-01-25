package lumi.insert.app.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.response.CategoryCreateResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {

        if(categoryRepository.existsByName(request.getName())){
            throw  new IllegalArgumentException("Category with the same name already exists.");
        } else {
            Category newCategory = Category.builder()
                .name(request.getName())
                .build();

            Category savedCategory = categoryRepository.save(newCategory);

            CategoryCreateResponse responseCategory = CategoryCreateResponse.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .createdAt(savedCategory.getCreatedAt())
                .updatedAt(savedCategory.getUpdatedAt())
                .build();

            return responseCategory;
        }
    }
    
}
