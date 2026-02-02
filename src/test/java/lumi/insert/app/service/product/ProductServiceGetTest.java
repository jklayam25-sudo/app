package lumi.insert.app.service.product;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import lumi.insert.app.dto.request.PaginationRequest;
import lumi.insert.app.dto.request.ProductGetByFilter;
import lumi.insert.app.dto.request.ProductGetNameRequest;
import lumi.insert.app.dto.response.ProductName;
import lumi.insert.app.dto.response.ProductResponse;
import lumi.insert.app.dto.response.ProductStockResponse;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;

public class ProductServiceGetTest extends BaseProductServiceTest{
    
    @Test
    public void testGetProductStockWithValidId() {
        when(productRepositoryMock.getStockById(1L)).thenReturn(Optional.of(50L));
        ProductStockResponse productStock = productServiceMock.getProductStock(1L);

        assertEquals(50L, productStock.stockQuantity());
        assertEquals(1L, productStock.id());

        verify(productRepositoryMock, times(1)).getStockById(1L);
    }

    @Test
    public void testGetProductStockWithInvalidId() {
        when(productRepositoryMock.getStockById(2L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.getProductStock(2L));

        verify(productRepositoryMock, times(1)).getStockById(2L);
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

        when(productRepositoryMock.findAllByNameContainingAndIsActiveTrue(eq("Pro"), any(Pageable.class))).thenReturn(productSlice);

        ProductGetNameRequest request = ProductGetNameRequest.builder()
        .name("Pro")
        .page(0)
        .size(5)
        .build();

        Slice<ProductName> allProductNames = productServiceMock.getAllProductNames(request);

        assertEquals(12, allProductNames.getNumberOfElements());
        assertEquals("Product 1", allProductNames.getContent().get(0).name());
        assertEquals("Product 2", allProductNames.getContent().get(1).name());
        assertEquals("Product 3", allProductNames.getContent().get(2).name());
        assertEquals("Product 4", allProductNames.getContent().get(3).name());
        assertFalse(allProductNames.hasNext());
    }

    @Test
    public void testGetProductNameByQueryNotFound(){

        Slice<Product> productSlice = new SliceImpl<>(List.of());

        when(productRepositoryMock.findAllByNameContainingAndIsActiveTrue(eq("Pro"), any(Pageable.class))).thenReturn(productSlice);

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

        ProductResponse productById = productServiceMock.getProductById(1L);
        verify(productMapper, times(1)).createDtoResponseFromProduct(mockProduct);

        assertEquals("NIKE Jordan Low 3", productById.name());
        assertEquals(10000L, productById.basePrice());
        assertEquals(12000L, productById.sellPrice());
        assertEquals(50L, productById.stockQuantity());
        assertEquals(5L, productById.stockMinimum());
        assertEquals(1L, productById.id());
        assertNull(productById.category());
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


        ProductResponse productById = productServiceMock.getProductById(1L);

        verify(productMapper, times(1)).createDtoResponseFromProduct(mockProduct);
        assertEquals("NIKE Jordan Low 3", productById.name());
        assertEquals(10000L, productById.basePrice());
        assertEquals(12000L, productById.sellPrice());
        assertEquals(50L, productById.stockQuantity());
        assertEquals(5L, productById.stockMinimum());
        assertEquals(1L, productById.id());
        assertEquals(2L, productById.category().id());
        assertEquals("Shoes", productById.category().name());
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

        Slice<ProductResponse> allProducts = productServiceMock.getAllProducts(request);

        verify(productMapper, times(12)).createDtoResponseFromProduct(any(Product.class));

        assertEquals(12, allProducts.getNumberOfElements());
        assertFalse(allProducts.hasNext());

        List<ProductResponse> content = allProducts.getContent();
        content.forEach(e -> {
            assertNotNull(e.name());
            assertNotNull(e.basePrice());
            assertNotNull(e.sellPrice());
            assertNotNull(e.stockQuantity());
            assertNotNull(e.stockMinimum());
            assertNotNull(e.id());
        });
    }

    @Test
    public void testGetProductByCriteria(){

        for ( int i = 1; i <= 12; i++ ) {
            Product dumpProduct = Product.builder()
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            productRepository.save(dumpProduct);
        }

        Category category = Category.builder()
        .id(99999L)
        .build();

         Product dumpProduct = Product.builder()
            .name("Prodoteus XIJ ")
            .basePrice(5999L)
            .sellPrice(5999L)
            .stockQuantity(10L)
            .stockMinimum(0L)
            .category(category)
            .build();

        productRepository.save(dumpProduct);

        Product dumpProductInactive = Product.builder()
            .name("Product XIJ ")
            .basePrice(5999L)
            .sellPrice(5999L)
            .stockQuantity(10L)
            .stockMinimum(0L)
            .isActive(true)
            .build();

        productRepository.save(dumpProductInactive);

        ProductGetByFilter productGetByFilter = ProductGetByFilter.builder()
        .name("prod")
        .minPrice(2000L)
        .maxPrice(6000L)
        .page(0)
        .size(5)
        .sortBy("sellPrice")
        .sortDirection("ASC")
        .categoryId(99999L)
        .build();

        Slice<ProductResponse> productsByRequests = productService.getProductsByRequests(productGetByFilter);
        Sort sort = Sort.by("sellPrice").ascending();
        assertEquals(1, productsByRequests.getNumberOfElements());
        assertEquals(sort, productsByRequests.getSort());
        assertEquals(5999L, productsByRequests.getContent().getFirst().sellPrice());
        assertEquals(5999L, productsByRequests.getContent().getLast().sellPrice());
    }

    @Test
    public void testGetProductByCriteriaWithInvalidCategories(){
        when(categoryRepositoryMock.existsById(1L)).thenReturn(false);

        ProductGetByFilter productGetByFilter = ProductGetByFilter.builder()
        .name("prod")
        .minPrice(2000L)
        .maxPrice(6000L)
        .page(0)
        .size(5)
        .sortBy("sellPrice")
        .sortDirection("ASC")
        .categoryId(1L)
        .build();

        assertThrows(IllegalArgumentException.class, () -> productServiceMock.getProductsByRequests(productGetByFilter));
    }


}
