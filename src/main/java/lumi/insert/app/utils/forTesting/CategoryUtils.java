package lumi.insert.app.utils.forTesting;
 
import lumi.insert.app.entity.Category;

public class CategoryUtils {
    
    public static Category getMockCategory(){
        Category category = Category.builder()
        .id(1L)
        .name("Category")
        .totalItems(0L)
        .isActive(true)
        .build();

        return category;
    }
}
