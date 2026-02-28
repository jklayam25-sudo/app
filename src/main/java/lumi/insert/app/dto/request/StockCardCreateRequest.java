package lumi.insert.app.dto.request;

import java.util.UUID;
  
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data; 


@Data
@Builder
public class StockCardCreateRequest {
    
    @NotNull(message = "referenceId cannot be empty") 
    private UUID referenceId;
 
    @NotNull(message = "productId cannot be empty")
    private Long productId; 
 
    @NotNull(message = "quantity cannot be empty and value atleas 1") 
    private Long quantity; 
 
    @NotBlank(message = "type cannot be empty")
    @Pattern(regexp = "CUSTOMER_IN|CUSTOMER_OUT|DEFECT|REPAIRED|SUPPLIER_IN|SUPPLIER_OUT|", message = "check documentation for type")
    private String type;

    private String description;

}
