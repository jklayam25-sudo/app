package lumi.insert.app.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lumi.insert.app.entity.nondatabase.StockMove;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false) 
public class StockCardGetByFilter extends PaginationRequest{
    
    UUID lastId;

    Long productId;

    StockMove type;

    LocalDateTime minCreatedAt;

    LocalDateTime maxCreatedAt;

    @Builder.Default 
    String sortBy = "createdAt";

    @Builder.Default
    @Pattern(regexp = "DESC|ASC", message = "check documentation for sortDirection specification")
    String sortDirection = "DESC";
}
