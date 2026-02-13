package lumi.insert.app.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder; 

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TransactionPaymentGetByFilter extends PaginationRequest{
 
    UUID transactionId;

    @Builder.Default
    @Min(value = 0, message = "minTotalPayment minimal value is 0")
    Long minTotalPayment = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalPayment minimal value is 0")
    Long maxTotalPayment = 9999999999990L;

    LocalDateTime minCreatedAt;

    LocalDateTime maxCreatedAt;

    @Builder.Default
    @Pattern(regexp = "createdAt|updatedAt|totalPayment", message = "check documentation for sortBy specification")
    String sortBy = "createdAt";

    @Builder.Default
    @Pattern(regexp = "DESC|ASC", message = "check documentation for sortDirection specification")
    String sortDirection = "DESC";

}
