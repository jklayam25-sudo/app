package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductEditRequest {
    
    @NotNull
    private Long id;

    private String name;

    private Long basePrice;

    private Long sellPrice;

    private Long stockQuantity;

    private Long stockMinimum;

    private Long categoryId;

}
