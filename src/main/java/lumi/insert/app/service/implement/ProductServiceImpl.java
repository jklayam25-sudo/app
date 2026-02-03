package lumi.insert.app.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.dto.request.ProductGetByFilter;
import lumi.insert.app.dto.request.ProductGetNameRequest;
import lumi.insert.app.dto.response.ProductDeleteResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.dto.response.ProductStockResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;
import lumi.insert.app.exception.BoilerplateRequestException;
import lumi.insert.app.exception.DuplicateEntityException;
import lumi.insert.app.exception.NotFoundEntityException;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.service.ProductService;
import lumi.insert.app.utils.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByName(request.getName())) {
            throw new DuplicateEntityException("Product with name " + request.getName() + " already exists");
        }

        Product newProduct = Product.builder()
            .name(request.getName())
            .basePrice(request.getBasePrice())
            .sellPrice(request.getSellPrice())
            .stockQuantity(request.getStockQuantity())
            .build();

        if(request.getStockMinimum() != null) {
            newProduct.setStockMinimum(request.getStockMinimum());
        }

        if(request.getCategoryId() != null) {
            Category searchedCategory = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new NotFoundEntityException("Category with ID " + request.getCategoryId() + " was not found"));

            newProduct.setCategory(searchedCategory);

            searchedCategory.setTotalItems(searchedCategory.getTotalItems() + 1);
            categoryRepository.save(searchedCategory); 
        }

        Product savedProduct = productRepository.save(newProduct);

        ProductResponse dtoResponseFromProduct = productMapper.createDtoResponseFromProduct(savedProduct);
        
        return dtoResponseFromProduct;
    }


    @Override
    public ProductStockResponse getProductStock(Long productId) {
        Long stock = productRepository.getStockById(productId).orElseThrow(() -> new NotFoundEntityException("Product with ID " + productId + " was not found"));

        ProductStockResponse responseStock = ProductStockResponse.builder()
            .id(productId)
            .stockQuantity(stock)
            .build();

        return responseStock;
    }


    @Override
    public ProductResponse editProduct(ProductEditRequest request) {
        Product existingProduct = productRepository.findById(request.getId()).orElseThrow(() -> new NotFoundEntityException("Product with ID " + request.getId() + " was not found"));

        productMapper.updateProductFromDto(request, existingProduct);
        Category category = existingProduct.getCategory();
        Long newCategoryId = request.getCategoryId();

        if(newCategoryId != null && (category == null || !category.getId().equals(newCategoryId))){
            if(category != null) {
                category.setTotalItems(category.getTotalItems() - 1L);
                categoryRepository.save(category);
            }

            Category newCategory = categoryRepository.findById(newCategoryId).orElseThrow(() -> new NotFoundEntityException("Category with ID " + newCategoryId + " was not found"));
            existingProduct.setCategory(newCategory);
            newCategory.setTotalItems(newCategory.getTotalItems() + 1L);

            categoryRepository.save(newCategory);
        }

        Product updatedProduct = productRepository.save(existingProduct);

        ProductResponse dtoResponseFromProduct = productMapper.createDtoResponseFromProduct(updatedProduct);
        
        return dtoResponseFromProduct;
    }


    @Override
    public Slice<ProductName> getAllProductNames(ProductGetNameRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<Product> allByNameContaining = productRepository.findAllByNameContainingAndIsActiveTrue(request.getName(), pageable);

        Slice<ProductName> map = allByNameContaining.map(product -> {
            ProductName productNameResponse = ProductName.builder()
                .id(product.getId())
                .name(product.getName())
                .build();
        
            return productNameResponse;
        });

        return map;
    }


    @Override
    public ProductResponse getProductById(Long id) {
        Product searchedProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Product with ID " + id + " was not found"));
 
        ProductResponse responseProduct = productMapper.createDtoResponseFromProduct(searchedProduct);
        return responseProduct;
    }


    @Override
    public Slice<ProductResponse> getAllProducts(PaginationRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<Product> allRawProducts = productRepository.findAllBy(pageable);
        Slice<ProductResponse> mapResult = allRawProducts.map(productMapper::createDtoResponseFromProduct);
        
        return mapResult;
    }


    @Override
    public Slice<ProductResponse> getProductsByRequests(ProductGetByFilter request) {
        if(!(request.getCategoryId() != null & categoryRepository.existsById(request.getCategoryId()))){
            throw new NotFoundEntityException("Category with ID " + request.getCategoryId() + " was not found");
        }

        Sort sort = Sort.by(request.getSortBy());

        if(request.getSortDirection().equalsIgnoreCase("DESC")){
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<Product> specification = (root, criteria, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();

            if(request.getCategoryId() != null){
                predicates.add(builder.equal(root.get("category").get("id"), request.getCategoryId()));
            }
            if (request.getName() != null) {
                predicates.add(builder.like(builder.lower(root.get("name")), "%" + request.getName() + "%"));
            }
            predicates.add(builder.isTrue(root.get("isActive")));
            predicates.add(builder.between(root.get("sellPrice"), request.getMinPrice(), request.getMaxPrice()));

            return builder.and(predicates);
        }; 

        Slice<Product> result = productRepository.findAll(specification, pageable);
        Slice<ProductResponse> resultMap = result.map(productMapper::createDtoResponseFromProduct);;
        return resultMap;
    }


    @Override
    public ProductDeleteResponse setProductInactive(Long id) {
        Product searchedProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Category with ID " + id + " was not found"));
        if(!searchedProduct.getIsActive()) throw new BoilerplateRequestException("Product with ID " + id + " already inactive");

        Category category = searchedProduct.getCategory();

        if(category != null){
            category.setTotalItems(category.getTotalItems() - 1L);
            categoryRepository.save(category);
        }

        searchedProduct.setIsActive(false);
        Product savedProduct = productRepository.save(searchedProduct);

        ProductDeleteResponse deleteDtoResponseFromProduct = productMapper.createDeleteDtoResponseFromProduct(savedProduct);
        return deleteDtoResponseFromProduct;
    }


    @Override
    public ProductDeleteResponse setProductActive(Long id) {
        Product searchedProduct = productRepository.findById(id).orElseThrow(() -> new NotFoundEntityException("Product with ID " + id + " was not found"));
        if(searchedProduct.getIsActive()) throw new BoilerplateRequestException("Product with ID " + id + " already active");

        Category category = searchedProduct.getCategory();

            if(category != null){
                category.setTotalItems(category.getTotalItems() + 1L);
                categoryRepository.save(category);
            }
        searchedProduct.setIsActive(true);
        Product savedProduct = productRepository.save(searchedProduct);

        ProductDeleteResponse deleteDtoResponseFromProduct = productMapper.createDeleteDtoResponseFromProduct(savedProduct);
        return deleteDtoResponseFromProduct;
    }

}
