package lumi.insert.app.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

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
    Long minTotalPayment = 0L;

    @Builder.Default
    Long maxTotalPayment = 9999999999990L;

    LocalDateTime minCreatedAt;

    LocalDateTime maxCreatedAt;

    @Builder.Default
    String sortBy = "createdAt";

    @Builder.Default
    String sortDirection = "DESC";

}
