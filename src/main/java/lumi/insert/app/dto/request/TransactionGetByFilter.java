package lumi.insert.app.dto.request;

import java.time.LocalDateTime;

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
    Long minTotalItems = 0L;

    @Builder.Default
    Long maxTotalItems = 9999990L;

    @Builder.Default
    Long minGrandTotal = 0L;

    @Builder.Default
    Long maxGrandTotal = 9999999999990L;

    @Builder.Default
    Long minTotalUnpaid = 0L;

    @Builder.Default
    Long maxTotalUnpaid = 9999999999990L;

    @Builder.Default
    Long minTotalPaid = 0L;

    @Builder.Default
    Long maxTotalPaid = 9999999999990L;

    TransactionStatus status;

    LocalDateTime minCreatedAt;

    LocalDateTime maxCreatedAt;

    @Builder.Default
    String sortBy = "createdAt";

    @Builder.Default
    String sortDirection = "DESC";

}
