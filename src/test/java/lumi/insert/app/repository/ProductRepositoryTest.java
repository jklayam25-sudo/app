package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import jakarta.transaction.Transactional;
import lumi.insert.app.entity.Product;

@DataJpaTest
@Transactional
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

        productRepository.save(dumpProduct1);
        productRepository.save(dumpProduct2);

        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").ascending());

        Slice<Product> products = productRepository.findAllByNameContaining("Jordan", pageable);
        Slice<Product> productsNike = productRepository.findAllByNameContaining("NIKE", pageable);
        Slice<Product> productsSomething = productRepository.findAllByNameContaining("Something", pageable);

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
}
