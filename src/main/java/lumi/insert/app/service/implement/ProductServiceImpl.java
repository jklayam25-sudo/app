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

import lumi.insert.app.dto.response.CategorySimpleResponse;
import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.dto.response.ProductStockResponse;

import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;

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
    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        CategorySimpleResponse categorySimpleResponse = null;

        if (productRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Product with the same name already exists.");
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
            .orElseThrow(() -> new IllegalArgumentException("Category didn't exists."));

            newProduct.setCategory(searchedCategory);

            categorySimpleResponse = CategorySimpleResponse.builder()
                .id(searchedCategory.getId())
                .name(searchedCategory.getName())
                .build();
        }

        Product savedProduct = productRepository.save(newProduct);

        ProductCreateResponse responseProduct = ProductCreateResponse.builder()
            .id(savedProduct.getId())
            .name(savedProduct.getName())
            .basePrice(savedProduct.getBasePrice())
            .sellPrice(savedProduct.getSellPrice())
            .stockQuantity(savedProduct.getStockQuantity())
            .stockMinimum(savedProduct.getStockMinimum())
            .category(categorySimpleResponse)
            .createdAt(savedProduct.getCreatedAt())
            .updatedAt(savedProduct.getUpdatedAt())
            .build();
        
            return responseProduct;
    }


    @Override
    public ProductStockResponse getProductStock(Long productId) {
        Long stock = productRepository.getStockById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));

        ProductStockResponse responseStock = ProductStockResponse.builder()
            .id(productId)
            .stockQuantity(stock)
            .build();

        return responseStock;
    }


    @Override
    public ProductCreateResponse editProduct(ProductEditRequest request) {

        if(!productRepository.existsById(request.getId())) {
            throw new IllegalArgumentException("Product not found");
        }

        Product existingProduct = productRepository.findById(request.getId()).orElseThrow();

        productMapper.updateProductFromDto(request, existingProduct);

        Product updatedProduct = productRepository.save(existingProduct);

        ProductCreateResponse responseProduct = ProductCreateResponse.builder()
            .id(updatedProduct.getId())
            .name(updatedProduct.getName())
            .basePrice(updatedProduct.getBasePrice())
            .sellPrice(updatedProduct.getSellPrice())
            .stockQuantity(updatedProduct.getStockQuantity())
            .stockMinimum(updatedProduct.getStockMinimum())
            .createdAt(updatedProduct.getCreatedAt())
            .updatedAt(updatedProduct.getUpdatedAt())
            .build();

        return responseProduct;
    }


    @Override
    public Slice<ProductName> getAllProductNames(ProductGetNameRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<Product> allByNameContaining = productRepository.findAllByNameContaining(request.getName(), pageable);

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
    public ProductCreateResponse getProductById(Long id) {
        Product searchedProduct = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));


        
        ProductCreateResponse responseProduct = ProductCreateResponse.builder()
            .id(searchedProduct.getId())
            .name(searchedProduct.getName())
            .basePrice(searchedProduct.getBasePrice())
            .sellPrice(searchedProduct.getSellPrice())
            .stockQuantity(searchedProduct.getStockQuantity())
            .stockMinimum(searchedProduct.getStockMinimum())
            .createdAt(searchedProduct.getCreatedAt())
            .updatedAt(searchedProduct.getUpdatedAt())
            .build();

        if(searchedProduct.getCategory() != null) {
            CategorySimpleResponse categorySimpleResponse = CategorySimpleResponse.builder()
                .id(searchedProduct.getCategory().getId())
                .name(searchedProduct.getCategory().getName())
                .build();
            responseProduct.setCategory(categorySimpleResponse);
        }

        return responseProduct;
    }


    @Override
    public Slice<ProductCreateResponse> getAllProducts(PaginationRequest request) {
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize()).withSort(sort);

        Slice<Product> allRawProducts = productRepository.findAllBy(pageable);

        Slice<ProductCreateResponse> result = allRawProducts.map(product -> {
            ProductCreateResponse responseProduct = ProductCreateResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .basePrice(product.getBasePrice())
                .sellPrice(product.getSellPrice())
                .stockQuantity(product.getStockQuantity())
                .stockMinimum(product.getStockMinimum())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

                if(product.getCategory() != null) {
                    CategorySimpleResponse categorySimpleResponse = CategorySimpleResponse.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build();
                    responseProduct.setCategory(categorySimpleResponse);
                }

            return responseProduct;
        });
        
        return result;
    }


    @Override
    public Slice<ProductResponse> getProductsByRequests(ProductGetByFilter request) {
        if(!(request.getCategoryId() != null & categoryRepository.existsById(request.getCategoryId()))){
            throw new IllegalArgumentException("Category not found");
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
            predicates.add(builder.between(root.get("sellPrice"), request.getMinPrice(), request.getMaxPrice()));

            return builder.and(predicates);
        }; 

        Slice<Product> result = productRepository.findAll(specification, pageable);
        Slice<ProductResponse> resultMap = result.map(productMapper::createDtoResponseFromProduct);;

        return resultMap;
    }

}
