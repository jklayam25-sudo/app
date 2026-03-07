package lumi.insert.app.dto.request;
 
 
import jakarta.validation.constraints.Min;  
import lombok.Builder;
import lombok.Data; 

@Data
@Builder
public class SupplyUpdateRequest {
  
    private String invoiceId;
 
    private String description;
 
    @Min(value = 1, message = "totalFee cannot below 0")
    private Long totalFee; 

    @Min(value = 1, message = "totalDiscount cannot below 0")
    private Long totalDiscount;
 
}
