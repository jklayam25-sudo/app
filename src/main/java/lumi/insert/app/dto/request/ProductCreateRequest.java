package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCreateRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "basePrice cannot be empty")
    @Min(value = 0, message = "basePrice cannot below 0")
    private Long basePrice;

    @NotNull(message = "sellPrice cannot be empty")
    @Min(value = 0, message = "sellPrice cannot below 0")
    private Long sellPrice;

    @NotNull(message = "stockQuantity cannot be empty")
    @Min(value = 0, message = "stockQuantity cannot below 0")
    private Long stockQuantity;

    private Long stockMinimum;

    private Long categoryId;
}
