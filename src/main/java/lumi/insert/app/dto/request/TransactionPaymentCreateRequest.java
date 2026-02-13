package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionPaymentCreateRequest {
    
    @NotNull(message = "paymentFrom must not be null")
    String paymentFrom;

    @NotNull(message = "paymentTo must not be null")
    String paymentTo;

    @NotNull(message = "totalPayment must not be null")
    @Min(value = 1, message = "totalPayment minimal value is 1")
    Long totalPayment;

}
