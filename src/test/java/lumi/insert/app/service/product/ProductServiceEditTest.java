package lumi.insert.app.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import lumi.insert.app.dto.request.ProductEditRequest;
import lumi.insert.app.dto.response.ProductDeleteResponse;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;

public class ProductServiceEditTest extends BaseProductServiceTest {
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

        ProductResponse editedProduct = productService.editProduct(productEditRequest);

        assertEquals("NIKE Jordan Low 4", editedProduct.name());
        assertEquals(11000L, editedProduct.basePrice());
        assertEquals(13000L, editedProduct.sellPrice());
        assertEquals(50L, editedProduct.stockQuantity());
        assertEquals(2L, editedProduct.stockMinimum());
        assertEquals(savedProduct.getId(), editedProduct.id());
    }

    @Test
    public void testEditProductWithInvalidId() {

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
    public void testUpdateProductCategory(){
        Category category = Category.builder()
        .name("Shoes")
        .build();

        Category savedCategory = categoryRepository.save(category);

        assertEquals(0L, savedCategory.getTotalItems());

        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product savedProduct = productRepository.save(mockProduct);

        ProductEditRequest request = ProductEditRequest.builder()
        .id(savedProduct.getId())
        .categoryId(savedCategory.getId())
        .build();

        ProductResponse editProductResponse = productService.editProduct(request);
        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(() -> new IllegalArgumentException("N"));
        
        assertEquals(1L, updatedCategory.getTotalItems());
        assertEquals("NIKE Jordan Low 3", editProductResponse.name());
        assertEquals(savedProduct.getId(), editProductResponse.id());   
    }

    @Test
    public void testSetInactiveProduct_totalItemsOfCategoryShouldBeDecrease(){
        Category category = Category.builder()
        .name("Shoes")
        .totalItems(10L)
        .build();

        Category savedCategory = categoryRepository.save(category);

        assertEquals(10L, savedCategory.getTotalItems());

        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .category(savedCategory)
        .build();

        Product savedProduct = productRepository.save(mockProduct);

        ProductDeleteResponse setInactiveProduct = productService.setProductInactive(savedProduct.getId());

        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(() -> new IllegalArgumentException("N"));
        assertEquals(9L, updatedCategory.getTotalItems());

        assertFalse(setInactiveProduct.isActive());
    }

    @Test
    public void testSetActiveProduct_totalItemsOfCategoryShouldBeIncrease(){
        Category category = Category.builder()
        .name("Shoes")
        .totalItems(10L)
        .build();

        Category savedCategory = categoryRepository.save(category);

        assertEquals(10L, savedCategory.getTotalItems());

        Product mockProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .category(savedCategory)
        .isActive(false)
        .build();

        Product savedProduct = productRepository.save(mockProduct);

        ProductDeleteResponse setInactiveProduct = productService.setProductActive(savedProduct.getId());

        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(() -> new IllegalArgumentException("N"));
        assertEquals(11L, updatedCategory.getTotalItems());

        assertTrue(setInactiveProduct.isActive());
    }

    @Test
    public void testSetActiveProduct_shouldBeThrownBecauseOfAlreadyActive(){
        Product alreadyActiveProduct = Product.builder()
        .id(90L)
        .isActive(true)
        .build();

        when(productRepositoryMock.findById(90L)).thenReturn(Optional.of(alreadyActiveProduct));
        assertThrows(IllegalArgumentException.class, () -> productServiceMock.setProductActive(90L));
    }

    @Test
    public void testSetActiveProduct_shouldBeThrownBecauseOfNotFoundProduct(){
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productServiceMock.setProductActive(1L));
    }

    @Test
    public void testSetInactiveProduct_shouldBeThrownBecauseOfAlreadyActive(){
        Product alreadyInactiveProduct = Product.builder()
        .id(90L)
        .isActive(false)
        .build();

        when(productRepositoryMock.findById(90L)).thenReturn(Optional.of(alreadyInactiveProduct));
        assertThrows(IllegalArgumentException.class, () -> productServiceMock.setProductInactive(90L));
    }

    @Test
    public void testSetInactiveProduct_shouldBeThrownBecauseOfNotFoundProduct(){
        when(productRepositoryMock.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productServiceMock.setProductInactive(1L));
    }
}
