package lumi.insert.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import lumi.insert.app.dto.request.ProductCreateRequest;
import lumi.insert.app.dto.response.ProductCreateResponse;
import lumi.insert.app.dto.response.ProductStockResponse;
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
}
