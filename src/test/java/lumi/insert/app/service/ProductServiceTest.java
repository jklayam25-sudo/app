package lumi.insert.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.dto.request.ProductGetNameRequest;
import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductStockResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;
import lumi.insert.app.repository.CategoryRepository;
import lumi.insert.app.repository.ProductRepository;
import lumi.insert.app.service.implement.ProductServiceImpl;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    
    @InjectMocks
    ProductServiceImpl productServiceMock;

    @Mock
    ProductRepository productRepositoryMock;

    @Mock
    CategoryRepository categoryRepositoryMock;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Test
    public void testCreateProductUncategorizedValid(){
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
        .name("NIKE Flyway 3")
        .basePrice(100L)
        .sellPrice(120L)
        .categoryId(null)
        .stockQuantity(3L)
        .build();

        ProductCreateResponse createdProduct = productService.createProduct(productCreateRequest);

        assertEquals("NIKE Flyway 3", createdProduct.getName());
        assertEquals(100L, createdProduct.getBasePrice());
        assertEquals(120L, createdProduct.getSellPrice());
        assertEquals(3L, createdProduct.getStockQuantity());
        assertEquals(0L, createdProduct.getStockMinimum());
        assertNotNull(createdProduct.getId());
        assertNull(createdProduct.getCategory());
        assertNotNull(createdProduct.getCreatedAt());
        assertNotNull(createdProduct.getUpdatedAt());

        Product searchedProduct = productRepository.findById(createdProduct.getId()).orElseThrow(() -> new InvalidParameterException(""));

        assertEquals(createdProduct.getName(), searchedProduct.getName());
        assertEquals(createdProduct.getCategory(), searchedProduct.getCategory());
    }

    @Test
    public void testCreateProductWithExistingName() {
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        when(productRepositoryMock.existsByName("NIKE Jordan Low 3")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.createProduct(productCreateRequest));

    }

    @Test
    public void testCreateProductWithInvalidCategory() {
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .categoryId(12L)
        .build();

        when(productRepositoryMock.existsByName("NIKE Jordan Low 3")).thenReturn(false);
        when(categoryRepositoryMock.findById(12L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.createProduct(productCreateRequest));
    }

    @Test
    public void testGetProductStockWithValidId() {
        when(productRepositoryMock.getStockById(1L)).thenReturn(Optional.of(50L));
        ProductStockResponse productStock = productServiceMock.getProductStock(1L);

        assertEquals(50L, productStock.getStockQuantity());
        assertEquals(1L, productStock.getId());

        verify(productRepositoryMock, times(1)).getStockById(1L);
    }

    @Test
    public void testGetProductStockWithInvalidId() {
        when(productRepositoryMock.getStockById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.getProductStock(2L));

        verify(productRepositoryMock, times(1)).getStockById(2L);
    }

    @Test
    @Disabled(value = "Logical fallacy")
    public void testEditProductWithValidIdXX() {
        when(productRepositoryMock.existsById(1L)).thenReturn(true);

        Product existingProduct = Product.builder()
        .id(1L)
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        existingProduct.setCreatedAt(LocalDateTime.now());

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(existingProduct));

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
        .id(1L)
        .name("NIKE Jordan Low 4")
        .basePrice(11000L)
        .sellPrice(13000L)
        .stockMinimum(2L)
        .build();

        ProductCreateResponse editedProduct = productServiceMock.editProduct(productEditRequest);

        assertEquals("NIKE Jordan Low 4", editedProduct.getName());
        assertEquals(11000L, editedProduct.getBasePrice());
        assertEquals(13000L, editedProduct.getSellPrice());
        assertEquals(50L, editedProduct.getStockQuantity());
        assertEquals(2L, editedProduct.getStockMinimum());
        assertEquals(1L, editedProduct.getId());
    }

    @Test
    public void testEditProductWithValidId() {

        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product savedProduct = productRepository.save(mockProduct);

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
        .id(savedProduct.getId())
        .name("NIKE Jordan Low 4")
        .basePrice(11000L)
        .sellPrice(13000L)
        .stockMinimum(2L)
        .build();

        ProductCreateResponse editedProduct = productService.editProduct(productEditRequest);

        assertEquals("NIKE Jordan Low 4", editedProduct.getName());
        assertEquals(11000L, editedProduct.getBasePrice());
        assertEquals(13000L, editedProduct.getSellPrice());
        assertEquals(50L, editedProduct.getStockQuantity());
        assertEquals(2L, editedProduct.getStockMinimum());
        assertEquals(savedProduct.getId(), editedProduct.getId());
    }

    @Test
    public void testEditProductWithInvalidId() {
        when(productRepositoryMock.existsById(1L)).thenReturn(false);

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
        .id(1L)
        .name("NIKE Jordan Low 4")
        .basePrice(11000L)
        .sellPrice(13000L)
        .stockMinimum(2L)
        .build();

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.editProduct(productEditRequest));
    }

    @Test
    public void testGetProductNameByQuery(){
        List<Product> products = new ArrayList<Product>();

        for ( int i = 1; i <= 12; i++ ) {
            Product dumpProduct = Product.builder()
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            products.add(dumpProduct);
        }

        Slice<Product> productSlice = new SliceImpl<>(products);

        when(productRepositoryMock.findAllByNameContaining(eq("Pro"), any(Pageable.class))).thenReturn(productSlice);

        ProductGetNameRequest request = ProductGetNameRequest.builder()
        .name("Pro")
        .page(0)
        .size(5)
        .build();

        Slice<ProductName> allProductNames = productServiceMock.getAllProductNames(request);

        assertEquals(12, allProductNames.getNumberOfElements());
        assertEquals("Product 1", allProductNames.getContent().get(0).getName());
        assertEquals("Product 2", allProductNames.getContent().get(1).getName());
        assertEquals("Product 3", allProductNames.getContent().get(2).getName());
        assertEquals("Product 4", allProductNames.getContent().get(3).getName());
        assertFalse(allProductNames.hasNext());
    }

    @Test
    public void testGetProductNameByQueryNotFound(){

        Slice<Product> productSlice = new SliceImpl<>(List.of());

        when(productRepositoryMock.findAllByNameContaining(eq("Pro"), any(Pageable.class))).thenReturn(productSlice);

        ProductGetNameRequest request = ProductGetNameRequest.builder()
        .name("Pro")
        .page(0)
        .size(5)
        .build();

        Slice<ProductName> allProductNames = productServiceMock.getAllProductNames(request);

        assertEquals(0, allProductNames.getNumberOfElements());
        assertFalse(allProductNames.hasNext());
        assertTrue(allProductNames.isEmpty());
    }

    @Test
    public void testGetProductByIdValid(){
        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .id(1L)
        .build();

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(mockProduct));


        ProductCreateResponse productById = productServiceMock.getProductById(1L);

        assertEquals("NIKE Jordan Low 3", productById.getName());
        assertEquals(10000L, productById.getBasePrice());
        assertEquals(12000L, productById.getSellPrice());
        assertEquals(50L, productById.getStockQuantity());
        assertEquals(5L, productById.getStockMinimum());
        assertEquals(1L, productById.getId());
        assertNull(productById.getCategory());
    }

    @Test
    public void testGetProductByIdValidWithCategory(){
        Category mockCategory = Category.builder()
        .id(2L)
        .name("Shoes")
        .build();

        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .category(mockCategory)
        .id(1L)
        .build();

        when(productRepositoryMock.findById(1L)).thenReturn(Optional.of(mockProduct));


        ProductCreateResponse productById = productServiceMock.getProductById(1L);

        assertEquals("NIKE Jordan Low 3", productById.getName());
        assertEquals(10000L, productById.getBasePrice());
        assertEquals(12000L, productById.getSellPrice());
        assertEquals(50L, productById.getStockQuantity());
        assertEquals(5L, productById.getStockMinimum());
        assertEquals(1L, productById.getId());
        assertEquals(2L, productById.getCategory().getId());
        assertEquals("Shoes", productById.getCategory().getName());
    }

    @Test
    public void testGetProductByIdInvalid(){
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.getProductById(1L));
    }

    @Test
    public void testGetAllProducts(){
        List<Product> products = new ArrayList<Product>();

        for ( int i = 1; i <= 12; i++ ) {
            final Long ids = Long.valueOf(i);
            Product dumpProduct = Product.builder()
            .id(ids)
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            products.add(dumpProduct);
        }

        Slice<Product> productSlice = new SliceImpl<>(products);

        when(productRepositoryMock.findAllBy(any(Pageable.class))).thenReturn(productSlice);

        PaginationRequest request = PaginationRequest.builder()
        .page(0)
        .size(12)
        .build();

        Slice<ProductCreateResponse> allProducts = productServiceMock.getAllProducts(request);

        assertEquals(12, allProducts.getNumberOfElements());
        assertFalse(allProducts.hasNext());

        List<ProductCreateResponse> content = allProducts.getContent();
        content.forEach(e -> {
            assertNotNull(e.getName());
            assertNotNull(e.getBasePrice());
            assertNotNull(e.getSellPrice());
            assertNotNull(e.getStockQuantity());
            assertNotNull(e.getStockMinimum());
            assertNotNull(e.getId());
        });
    }

}
