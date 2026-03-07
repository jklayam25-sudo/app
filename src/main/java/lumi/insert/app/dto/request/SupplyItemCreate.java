package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor 
public class SupplyItemCreate {
    
    @NotNull(message = "productId cannot be empty")
    private Long productId;

    @NotNull(message = "price cannot be empty")
    @Min(value = 0, message = "price cannot below 0")
    private Long price;

    @NotNull(message = "quantity cannot be empty")
    @Min(value = 0, message = "quantity cannot below 0")
    private Long quantity;

    private String description;
}
