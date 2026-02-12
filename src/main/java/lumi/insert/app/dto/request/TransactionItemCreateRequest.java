package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionItemCreateRequest {
    
    @NotNull(message =  "productId must not be null")
    Long productId;

    @NotNull(message =  "quantity must not be null")
    @Min(message = "quantity minimal value is 1", value = 1)
    Long quantity;

}
