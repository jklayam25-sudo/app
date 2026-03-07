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
public class ItemRefundRequest {
    
    @NotNull(message = "productId cannot be empty")
    private Long productId; 

    @NotNull(message = "quantity cannot be empty")
    @Min(value = 1, message = "quantity cannot below 1")
    private Long quantity;

    private String description;
}
