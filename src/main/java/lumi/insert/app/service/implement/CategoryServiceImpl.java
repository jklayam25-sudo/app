package lumi.insert.app.service.implement;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lumi.insert.app.dto.request.CategoryCreateRequest;
import lumi.insert.app.dto.request.CategoryUpdateRequest;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.response.CategoryResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;
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
            throw  new DuplicateEntityException("Category with name " + request.getName() + " already exists");
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
    public CategoryResponse updateCategoryName(CategoryUpdateRequest request) {
        if(categoryRepository.existsByName(request.getName())){
            throw  new DuplicateEntityException("Category with name " + request.getName() + " already exists");
        } else {
        Category searchedCategory = categoryRepository.findById(request.getId()).orElseThrow(() -> new NotFoundEntityException("Category with ID " + request.getId() + " was not found"));
        
        searchedCategory.setName(request.getName());
        Category savedCategory = categoryRepository.save(searchedCategory);

        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
        }
    }

    @Override
    public CategoryResponse activateCategory(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Category with ID " + id + " was not found"));
        if(searchedCategory.getIsActive()) throw new BoilerplateRequestException("Category with ID " + id + " already active");

        searchedCategory.setIsActive(true);
        Category savedCategory = categoryRepository.save(searchedCategory);

        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse deactivateCategory(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Category with ID " + id + " was not found"));
        if(!searchedCategory.getIsActive()) throw new BoilerplateRequestException("Category with ID " + id + " already inactive");

        searchedCategory.setIsActive(false);
        Category savedCategory = categoryRepository.save(searchedCategory);
        
        CategoryResponse response = categoryMapper.createDtoResponseFromCategory(savedCategory);
        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category searchedCategory = categoryRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Category with ID " + id + " was not found"));

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

 