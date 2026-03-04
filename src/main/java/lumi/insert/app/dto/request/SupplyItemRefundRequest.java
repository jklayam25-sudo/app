package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SupplyItemRefundRequest {
    
    @NotNull(message = "productId cannot be empty")
    private Long productId; 

    @NotNull(message = "quantity cannot be empty")
    private Long quantity;

    private String description;
}
