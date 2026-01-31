package lumi.insert.app.service.implement;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.request.CategoryEditRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.CategoryResponse;

import lumi.insert.app.entity.Category; 
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.service.CategoryService;
import lumi.insert.app.utils.mapper.CategoryMapper;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest request) {

        if(categoryRepository.existsByName(request.getName())){
            throw  new IllegalArgumentException("Category with the same name already exists.");
        } else {
            Category newCategory = Category.builder()
                .name(request.getName())
                .build();

            Category savedCategory = categoryRepository.save(newCategory);

            CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);

            return response;
        }
    }

    @Override
    public CategoryResponse editCategoryName(CategoryEditRequest request) {
        Category searchedCategory = categoryRepository.findById(request.getId()).orElseThrow(() -> new IllegalArgumentException("Category not found!"));
        
        searchedCategory.setName(request.getName());
        Category savedCategory = categoryRepository.save(searchedCategory);

        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse setCategoryActive(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found!"));
        if(searchedCategory.getIsActive()) throw new IllegalArgumentException("Category already active");

        searchedCategory.setIsActive(true);
        Category savedCategory = categoryRepository.save(searchedCategory);

        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse setCategoryInactive(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found!"));
        if(!searchedCategory.getIsActive()) throw new IllegalArgumentException("Category already inactive");

        searchedCategory.setIsActive(false);
        Category savedCategory = categoryRepository.save(searchedCategory);
        
        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found!"));

        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(searchedCategory);

        return response;
    }

    @Override
    public Slice<CategoryResponse> getCategories(PaginationRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<Category> searchedCategories = categoryRepository.findAllByIsActiveTrue(pageable);
        Slice<CategoryResponse> response = searchedCategories.map(categoryMapper::createDtoResponseFromCategory);

        return response;
    }
    
}
