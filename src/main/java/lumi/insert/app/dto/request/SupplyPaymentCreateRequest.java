package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor; 
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor 
public class SupplyPaymentCreateRequest {
    
    @NotNull(message = "paymentFrom must not be null")
    String paymentFrom;

    @NotNull(message = "paymentTo must not be null")
    String paymentTo;

    @NotNull(message = "totalPayment must not be null")
    @Min(value = 1, message = "totalPayment minimal value is 1")
    Long totalPayment;

}
