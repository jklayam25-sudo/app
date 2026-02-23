package lumi.insert.app.dto.request;

import java.util.UUID;
 
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionCreateRequest {

    @NotNull(message = "customerId cannot be empty")
    private UUID customerId;
 
    
}
