package lumi.insert.app.dto.request;

import java.util.UUID;
 
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to create a new transaction")
public class TransactionCreateRequest {

    @NotNull(message = "customerId cannot be empty")
    @Schema(description = "Customer ID for this transaction", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID customerId;
 
    
}
