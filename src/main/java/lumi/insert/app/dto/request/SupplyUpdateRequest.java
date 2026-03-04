package lumi.insert.app.dto.request;
 
 
import jakarta.validation.constraints.Min; 
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data; 

@Data
@Builder
public class SupplyUpdateRequest {
 
    @NotNull(message = "invoiceId cannot be empty")
    private String invoiceId;
 
    private String description;

    @NotNull(message = "totalFee cannot be empty")
    @Min(value = 1)
    private Long totalFee;

    @NotNull(message = "totalDiscount cannot be empty")
    @Min(value = 1)
    private Long totalDiscount;
 
}
