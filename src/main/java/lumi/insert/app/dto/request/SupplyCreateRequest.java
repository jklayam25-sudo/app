package lumi.insert.app.dto.request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder; 

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor 
public class SupplyCreateRequest {

    @NotNull(message = "supplierId cannot be empty")
    private UUID supplierId;

    @NotNull(message = "invoiceId cannot be empty")
    private String invoiceId;

    @Size(min = 1, message = "items cannot be empty")
    @Builder.Default
    @Valid
    private List<SupplyItemCreate> supplyItems = new ArrayList<>();

    private String description;

    @NotNull(message = "totalFee cannot be empty")
    @Min(value = 0, message = "totalFee cannot below 0")
    private Long totalFee;

    @NotNull(message = "totalDiscount cannot be empty")
    @Min(value = 0, message = "totalDiscount cannot below 0")
    private Long totalDiscount;
 
}
