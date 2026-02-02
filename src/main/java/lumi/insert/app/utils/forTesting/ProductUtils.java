package lumi.insert.app.utils.forTesting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
 
import lumi.insert.app.entity.Product;
import lumi.insert.app.entity.Category;

public class ProductUtils {

    public static Slice<Product> getMockSliceProduct(){
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

        return productSlice;
    }

    public static Product getMockCategorizedProduct(){
        Category dumpCategory = Category.builder()
        .id(1L)
        .name("Category")
        .isActive(true)
        .totalItems(0L)
        .build();

        Product dumpProduct = Product.builder()
            .id(1L)
            .name("Product")
            .basePrice(1000L)
            .sellPrice(1200L)
            .stockQuantity(10L)
            .stockMinimum(1L)
            .category(dumpCategory)
            .build();

        return dumpProduct;
    }

}
