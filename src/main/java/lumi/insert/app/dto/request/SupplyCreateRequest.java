package lumi.insert.app.dto.request;

import java.util.List;
import java.util.UUID;
 
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data; 

@Data
@Builder
public class SupplyCreateRequest {

    @NotNull(message = "supplierId cannot be empty")
    private UUID supplierId;

    @NotNull(message = "invoiceId cannot be empty")
    private String invoiceId;

    @NotNull(message = "invoiceId cannot be empty")
    private List<SupplyItemCreate> items;

    private String description;

    @NotNull(message = "totalFee cannot be empty")
    private Long totalFee;

    @NotNull(message = "totalDiscount cannot be empty")
    private Long totalDiscount;
 
}
