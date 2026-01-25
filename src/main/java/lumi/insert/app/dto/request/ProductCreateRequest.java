package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreateRequest {

    @NotNull
    private String name;

    @NotNull   
    private Long basePrice;

    @NotNull
    private Long sellPrice;

    @NotNull
    private Long stockQuantity;

    private Long stockMinimum;

    private Long categoryId;
}
