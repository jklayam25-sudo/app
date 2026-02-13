package lumi.insert.app.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lumi.insert.app.entity.TransactionStatus;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class TransactionGetByFilter extends PaginationRequest{

    @Builder.Default
    @Min(value = 0, message = "minTotalItems minimal value is 0")
    Long minTotalItems = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalItems minimal value is 0")
    Long maxTotalItems = 9999990L;

    @Builder.Default
    @Min(value = 0, message = "minGrandTotal minimal value is 0")
    Long minGrandTotal = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxGrandTotal minimal value is 0")
    Long maxGrandTotal = 9999999999990L;

    @Builder.Default
    @Min(value = 0, message = "minTotalUnpaid minimal value is 0")
    Long minTotalUnpaid = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalUnpaid minimal value is 0")
    Long maxTotalUnpaid = 9999999999990L;

    @Builder.Default
    @Min(value = 0, message = "minTotalPaid minimal value is 0")
    Long minTotalPaid = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalPaid minimal value is 0")
    Long maxTotalPaid = 9999999999990L;

    TransactionStatus status;

    LocalDateTime minCreatedAt;

    LocalDateTime maxCreatedAt;

    @Builder.Default
    @Pattern(regexp = "createdAt|updatedAt|totalItems|totalFee|totalDiscount|subTotal|grandTotal|totalUnpaid|totalPaid|totalUnrefunded|totalRefunded", message = "check documentation for sortBy specification")
    String sortBy = "createdAt";

    @Builder.Default
    @Pattern(regexp = "DESC|ASC", message = "check documentation for sortDirection specification")
    String sortDirection = "DESC";

}
