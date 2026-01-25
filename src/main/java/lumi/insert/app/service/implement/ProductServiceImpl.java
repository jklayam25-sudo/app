package lumi.insert.app.service.implement;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.response.CategorySimpleResponse;
import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductStockResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;


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
    
}
