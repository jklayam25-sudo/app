package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
public class CustomerGetByFilter extends PaginationRequest{

    String name;

    String email;

    String contact;

    Boolean isActive;

    @Builder.Default
    @Min(value = 0, message = "minTotalItems minimal value is 0")
    Long minTotalTransaction = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalItems minimal value is 0")
    Long maxTotalTransaction = 9999990L;

    @Builder.Default
    @Min(value = 0, message = "minGrandTotal minimal value is 0")
    Long minTotalUnpaid = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxGrandTotal minimal value is 0")
    Long maxTotalUnpaid = 9999999999990L;

    @Builder.Default
    @Min(value = 0, message = "minTotalUnpaid minimal value is 0")
    Long minTotalPaid = 0L;

    @Builder.Default
    @Min(value = 0, message = "maxTotalUnpaid minimal value is 0")
    Long maxTotalPaid = 9999999999990L;

    @Builder.Default
    @Pattern(regexp = "createdAt|updatedAt|totalTransaction|totalUnpaid|totalPaid|name", message = "check documentation for sortBy specification")
    String sortBy = "createdAt";

    @Builder.Default
    @Pattern(regexp = "DESC|ASC", message = "check documentation for sortDirection specification")
    String sortDirection = "DESC";
}
