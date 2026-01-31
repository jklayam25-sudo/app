package lumi.insert.app.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import lumi.insert.app.dto.request.ProductCreateRequest; 
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.entity.Product;

public class ProductServiceCreateTest extends BaseProductServiceTest{
    
    @Test
    public void testCreateProductUncategorizedValid(){
        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
        .name("NIKE Flyway 3")
        .basePrice(100L)
        .sellPrice(120L)
        .categoryId(null)
        .stockQuantity(3L)
        .build();

        ProductResponse createdProduct = productService.createProduct(productCreateRequest);

        assertEquals("NIKE Flyway 3", createdProduct.name());
        assertEquals(100L, createdProduct.basePrice());
        assertEquals(120L, createdProduct.sellPrice());
        assertEquals(3L, createdProduct.stockQuantity());
        assertEquals(0L, createdProduct.stockMinimum());
        assertNotNull(createdProduct.id());
        assertNull(createdProduct.category());
        assertNotNull(createdProduct.createdAt());
        assertNotNull(createdProduct.updatedAt());

        Product searchedProduct = productRepository.findById(createdProduct.id()).orElseThrow(() -> new InvalidParameterException(""));

        assertEquals(createdProduct.name(), searchedProduct.getName());
        assertEquals(createdProduct.category(), searchedProduct.getCategory());
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
}
