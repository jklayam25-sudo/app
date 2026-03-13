package lumi.insert.app.dto.request;
 
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data; 

@Data
@Builder
public class ProductStatisticExportRequest {
       
    @Schema(description = "Start date for statistics range", example = "2024-01-01T00:00:00")
    private LocalDateTime startDate;
 
    @Schema(description = "End date for statistics range", example = "2024-01-01T00:00:00")
    private LocalDateTime endDate; 

    @Builder.Default
    @Schema(description = "Generated data type implementation", example = "pdf/xlsx")
    @Pattern(regexp = "pdf|xlsx", message = "check documentation for export type specification")
    private String type = "pdf";

}
