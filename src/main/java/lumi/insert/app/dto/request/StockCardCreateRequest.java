package lumi.insert.app.dto.request;

import java.util.UUID;
 
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data; 


@Data
@Builder
public class StockCardCreateRequest {
    
    @NotBlank(message = "referenceId cannot be empty") 
    private UUID referenceId;
 
    @NotNull(message = "productId cannot be empty")
    private Long productId; 
 
    @NotNull(message = "quantity cannot be empty and value atleas 1")
    @Min(value = 1)
    private Long quantity; 
 
    @NotBlank(message = "type cannot be empty")
    private String type;

    private String description;

}
