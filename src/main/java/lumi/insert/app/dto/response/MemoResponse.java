package lumi.insert.app.dto.response;

import java.util.List;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Builder
@Schema(description = "Response object representing an internal memo or announcement for employees")
public record MemoResponse(
    
    @Schema(description = "Unique identifier of the memo record", example = "101")
    Long id, 
    
    @Schema(description = "The subject or headline of the memo", example = "Update Promo Weekend Batam")
    String title, 
    
    @Schema(description = "The main content or detailed message", example = "Semua transaksi di atas 500rb dapat diskon 5% khusus hari ini.")
    String body, 
    
    @ArraySchema(schema = @Schema(description = "List of URLs for attached images or screenshots", example = "https://cdn.lumi.app/memos/promo-banner.jpg"))
    List<String> images, 
    
    @Schema(description = "The target role authorized to see this memo", example = "CASHIER")
    EmployeeRole role, 
    
    @Schema(description = "Status indicating if the current logged-in user has read this memo", example = "false")
    Boolean isRead
) {
    
}