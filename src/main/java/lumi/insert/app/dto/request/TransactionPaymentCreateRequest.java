package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionPaymentCreateRequest {
    
    @NotNull
    String paymentFrom;

    @NotNull
    String paymentTo;

    @NotNull
    @Min(value = 1)
    Long totalPayment;

}
