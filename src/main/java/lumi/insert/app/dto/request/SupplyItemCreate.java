package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SupplyItemCreate {
    
    @NotNull(message = "productId cannot be empty")
    private Long productId;

    @NotNull(message = "price cannot be empty")
    private Long price;

    @NotNull(message = "quantity cannot be empty")
    private Long quantity;

    private String description;
}
