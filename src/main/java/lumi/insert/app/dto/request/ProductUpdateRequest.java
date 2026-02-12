package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min; 
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpdateRequest {
     
    private Long id;

    private String name;

    @Min(value = 0, message = "basePrice cannot below 0")
    private Long basePrice;

    @Min(value = 0, message = "sellPrice cannot below 0")
    private Long sellPrice;

    @Min(value = 0, message = "stockQuantity cannot below 0")
    private Long stockQuantity;

    @Min(value = 0, message = "stockMinimum cannot below 0")
    private Long stockMinimum;

    private Long categoryId;

}
