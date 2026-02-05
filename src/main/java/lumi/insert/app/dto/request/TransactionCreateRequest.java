package lumi.insert.app.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionCreateRequest {

    @NotBlank(message = "customerId cannot be empty")
    private UUID customerId;

    @NotBlank(message = "staffId cannot be empty")
    private UUID staffId;
    
}
