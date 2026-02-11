package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.entity.Product;
import lumi.insert.app.repository.projection.ProductRefreshProjection;

@DataJpaTest
@Transactional
@Slf4j
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        Product dumpProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product savedProduct = productRepository.save(dumpProduct);

        Optional<Product> byId = productRepository.findById(savedProduct.getId());

        if( byId.isPresent() ) {
            Product foundProduct = byId.get();
            assertEquals("NIKE Jordan Low 3", foundProduct.getName());
            assertEquals(10000L, foundProduct.getBasePrice());
            assertEquals(12000L, foundProduct.getSellPrice());
            assertEquals(50L, foundProduct.getStockQuantity());
            assertEquals(5L, foundProduct.getStockMinimum());
        } else {
            Assertions.fail("Product not found");
        }
    }

    @Test
    public void testGetStockById() {
        Product dumpProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product savedProduct = productRepository.save(dumpProduct);

        Optional<Long> stockProjection = productRepository.getStockById(savedProduct.getId());

        assertEquals(50L, stockProjection.get());
    }

    @Test
    public void testFindAllByName() {
        Product dumpProduct1 = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product dumpProduct2 = Product.builder()
        .name("NIKE Kyrie 5")
        .basePrice(11000L)
        .sellPrice(13000L)
        .stockQuantity(30L)
        .stockMinimum(3L)
        .build();

        Product dumpProductInactive = Product.builder()
        .name("NIKE Jordan Low Inactive")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .isActive(false)
        .build();

        productRepository.save(dumpProduct1);
        productRepository.save(dumpProduct2);
        productRepository.save(dumpProductInactive);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

        Slice<Product> products = productRepository.findAllByNameContainingAndIsActiveTrue("Jordan", pageable);
        Slice<Product> productsNike = productRepository.findAllByNameContainingAndIsActiveTrue("NIKE", pageable);
        Slice<Product> productsSomething = productRepository.findAllByNameContainingAndIsActiveTrue("Something", pageable);

        assertEquals(1, products.getNumberOfElements());
        assertEquals(2, productsNike.getNumberOfElements());
        assertFalse(productsNike.hasNext());
        assertTrue(productsSomething.isEmpty());
    }

    @Test
    public void testFindAllPagination() {
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
        Pageable pageable = PageRequest.of(0, 5, Sort.by("sellPrice").ascending());
        Slice<Product> products = productRepository.findAllBy(pageable);
        assertTrue(products.hasNext());
        assertEquals(5, products.getNumberOfElements());
        List<Product> productsSet = products.getContent();
        System.out.println(productsSet);

        assertEquals("Product 1", productsSet.getFirst().getName());
        assertEquals("Product 5", productsSet.getLast().getName());
        
        if( products.hasNext() ) {
            pageable = products.nextPageable();
            products = productRepository.findAllBy(pageable);
            assertTrue(products.hasNext());
            assertEquals(5, products.getNumberOfElements());
            productsSet = products.getContent();

            assertEquals("Product 6", productsSet.getFirst().getName());
            assertEquals("Product 10", productsSet.getLast().getName());

            if( products.hasNext() ) {
                pageable = products.nextPageable();
                products = productRepository.findAllBy(pageable);
                assertFalse(products.hasNext());
                assertEquals(2, products.getNumberOfElements());
                productsSet = products.getContent();

                assertEquals("Product 11", productsSet.getFirst().getName());
                assertEquals("Product 12", productsSet.getLast().getName());
            }
        }
    }

    @Test
    public void testExistsByName() {
        Product dumpProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        productRepository.save(dumpProduct);

        Boolean exists = productRepository.existsByName("NIKE Jordan Low 3");
        assertTrue(exists);

        Boolean notExists = productRepository.existsByName("Adidas Superstar");
        assertFalse(notExists);
    }

    @Test
    public void testGetStockByIdNotFound() {
        Optional<Long> stockProjection = productRepository.getStockById(999L);
        assertEquals(null, stockProjection.orElse(null));
    }

    @Test
    public void testGetProductCriteria(){
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

        Pageable pageable = PageRequest.of(0, 5, Sort.by("sellPrice").ascending());

        Specification<Product> specification = (root, criteria, builder) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(builder.like(builder.lower(root.get("name")), "%" + "pro" + "%"));
            predicates.add(builder.between(root.get("sellPrice"), 0L, 5000L));

            return builder.and(predicates);
        }; 

       Slice<Product> result = productRepository.findAll(specification, pageable);

        System.out.println(result.getContent());
        assertEquals(4, result.getNumberOfElements());
        assertEquals(4800L, result.getContent().getLast().getSellPrice());
    }

    @Test
    @DisplayName("Should return List of ProductRefreshProjection<id, sPrice, stockQ>")
    public void searchIdUpdatedAtMoreThan_validListId_returnListProductRefreshProjection(){
        LocalDateTime time = LocalDateTime.now();
        List<Long> ids = new ArrayList<>();
        for ( int i = 1; i <= 2; i++ ) {
            Product dumpProduct = Product.builder()
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            Product saved = productRepository.save(dumpProduct);
            ids.add(saved.getId());
        }
       List<ProductRefreshProjection> searchIdUpdatedAtMoreThan = productRepository.searchIdUpdatedAtMoreThan(ids, time);
       log.info("x{}", searchIdUpdatedAtMoreThan.getLast());
       assertEquals(2, searchIdUpdatedAtMoreThan.size());
       assertEquals(2400L, searchIdUpdatedAtMoreThan.getLast().sellPrice());
    }
    
    @Test
    @DisplayName("Should return List of ProductRefreshProjection<id, sPrice, stockQ>")
    public void searchIdUpdatedAtMoreThan_timeIsNewer_return0ProductRefreshProjection(){ 
        List<Long> ids = new ArrayList<>();
        for ( int i = 1; i <= 2; i++ ) {
            Product dumpProduct = Product.builder()
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            Product saved = productRepository.save(dumpProduct);
            ids.add(saved.getId());
        }
       List<ProductRefreshProjection> searchIdUpdatedAtMoreThan = productRepository.searchIdUpdatedAtMoreThan(ids, LocalDateTime.now().plusDays(1));
       assertEquals(0, searchIdUpdatedAtMoreThan.size()); 
    }

    @Test
    @DisplayName("Should return List of Product Entity")
    public void searchProductUpdatedAtMoreThan_validListId_returnListProduct(){
        LocalDateTime time = LocalDateTime.now();
        List<Long> ids = new ArrayList<>();
        for ( int i = 1; i <= 2; i++ ) {
            Product dumpProduct = Product.builder()
            .name("Product " + i)
            .basePrice(1000L * i)
            .sellPrice(1200L * i)
            .stockQuantity(10L * i)
            .stockMinimum(1L * i)
            .build();

            Product saved = productRepository.save(dumpProduct);
            ids.add(saved.getId());
        }
       List<Product> searchIdUpdatedAtMoreThan = productRepository.searchProductUpdatedAtMoreThan(ids, time); 
       assertEquals(2, searchIdUpdatedAtMoreThan.size());
       assertEquals(2400L, searchIdUpdatedAtMoreThan.getLast().getSellPrice());
    }
}
