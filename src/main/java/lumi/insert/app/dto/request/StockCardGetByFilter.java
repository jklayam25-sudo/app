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
import lumi.insert.app.core.entity.nondatabase.StockMove;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Schema(description = "Filter request for searching stock cards")
public class StockCardGetByFilter extends PaginationRequest{
    
    @Schema(description = "Last stock card ID for pagination", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID lastId;

    @Schema(description = "Filter by product ID", example = "1")
    Long productId;

    @Schema(description = "Filter by stock move type", example = "IN")
    StockMove type;

    @Schema(description = "Start date for stock card range", example = "2024-01-01T00:00:00")
    LocalDateTime minCreatedAt;

    @Schema(description = "End date for stock card range", example = "2024-12-31T23:59:59")
    LocalDateTime maxCreatedAt;

    @Builder.Default 
    String sortBy = "createdAt";

    @Builder.Default
    @Pattern(regexp = "DESC|ASC", message = "check documentation for sortDirection specification")
    String sortDirection = "DESC";
}
