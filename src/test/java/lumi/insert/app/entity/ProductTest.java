package lumi.insert.app.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class ProductTest {
    
    @Test
    public void testCreateProduct() {
        Product dumpProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .build();

        assertNotNull(dumpProduct);
        assertEquals("NIKE Jordan Low 3", dumpProduct.getName());
        assertEquals(10000L, dumpProduct.getBasePrice());
        assertEquals(12000L, dumpProduct.getSellPrice());
        assertEquals(50L, dumpProduct.getStockQuantity());
        assertEquals(5L, dumpProduct.getStockMinimum());
    }

    @Test
    public void testCreateProductWithCategory() {
        Category dumpCategory = Category.builder()
        .name("Shoes")
        .build();

        Product dumpProduct = Product.builder()
        .name("NIKE Jordan Low 3")
        .basePrice(10000L)
        .sellPrice(12000L)
        .stockQuantity(50L)
        .stockMinimum(5L)
        .category(dumpCategory)
        .build();

        assertNotNull(dumpProduct);
        assertSame(dumpCategory, dumpProduct.getCategory());
        assertEquals("NIKE Jordan Low 3", dumpProduct.getName());
        assertEquals(10000L, dumpProduct.getBasePrice());
        assertEquals(12000L, dumpProduct.getSellPrice());
        assertEquals(50L, dumpProduct.getStockQuantity());
        assertEquals(5L, dumpProduct.getStockMinimum());
    }

}
