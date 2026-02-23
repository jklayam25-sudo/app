package lumi.insert.app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import lumi.insert.app.entity.Category;
import lumi.insert.app.entity.Product;

@DataJpaTest
@Transactional
@Slf4j
public class CategoryRepositoryTest {
    
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    public void testSaveCategory() {
        Category dumpCategory = Category.builder()
        .name("Shoes")
        .build();

        Category savedCategory = categoryRepository.save(dumpCategory);

        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow();

        assertEquals("Shoes", foundCategory.getName());
    }

    @Test
    public void testFindProductFromCategory() {
        Category dumpCategory = Category.builder()
        .name("Shoes")
        .build();

        Category savedCategory = categoryRepository.save(dumpCategory);

        Product dumpCategoriedProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .category(savedCategory)
        .build();

        Product dumpProduct = Product.builder()
        .name("PUMA Running Pro")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        Product savedCategoriedProduct = productRepository.save(dumpCategoriedProduct);
        Product savedProduct = productRepository.save(dumpProduct);

        assertNotSame(savedCategoriedProduct, savedProduct);

        categoryRepository.flush();
        entityManager.clear();
        
        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow();
        log.info("Found category: {}", foundCategory);
        List<Product> product = foundCategory.getProduct();
        assertEquals(1, product.size());
        assertEquals("NIKE Jordan Low 3", product.getFirst().getName());
    }

    @Test
    public void testExistsByName() {
        Category dumpCategory = Category.builder()
        .name("Shoes")
        .build();

        categoryRepository.save(dumpCategory);

        boolean exists = categoryRepository.existsByName("Shoes");
        assertEquals(true, exists);

        boolean notExists = categoryRepository.existsByName("Electronics");
        assertEquals(false, notExists);
    }

    @Test
    public void testFindAllByIsActiveTrue_shouldReturnOnlyActiveCategory(){
        Category dumpCategory = Category.builder()
        .name("Shoes Active")
        .build();

        Category dumpCategoryInactive = Category.builder()
        .name("Shoes Inactive")
        .isActive(false)
        .build();

        categoryRepository.saveAll(List.of(dumpCategory, dumpCategoryInactive));

        Slice<Category> allByIsActiveTrue = categoryRepository.findAllByIsActiveTrue(PageRequest.of(0, 5));

        assertEquals(1, allByIsActiveTrue.getNumberOfElements());
    }

    @Test
    public void testFindAllByIsInactiveTrue_shouldReturnOnlyInactiveCategory(){
        Category dumpCategory = Category.builder()
        .name("Shoes Active")
        .build();

        Category dumpCategoryInactive = Category.builder()
        .name("Shoes Inactive")
        .isActive(false)
        .build();

        Category dumpCategoryInactive2 = Category.builder()
        .name("Shoes Inactive 2")
        .isActive(false)
        .build();

        categoryRepository.saveAll(List.of(dumpCategory, dumpCategoryInactive, dumpCategoryInactive2));

        Slice<Category> allByIsActiveTrue = categoryRepository.findAllByIsActiveFalse(PageRequest.of(0, 5));

        assertEquals(2, allByIsActiveTrue.getNumberOfElements());
    }
}
