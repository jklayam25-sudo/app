package lumi.insert.app.dto.request;
 

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to update an existing memo")
public class MemoUpdateRequest { 

    @Size(min = 1, message = "title length must be atleas 1")
    @Schema(description = "Updated memo title", example = "Important Announcement")
    String title;
 
    @Size(min = 1, message = "body length must be atleas 1")
    @Schema(description = "Updated memo content", example = "This is the updated announcement.")
    String body;

    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE|OWNER", message = "check documentation for role specification")
    @Schema(description = "Target role for this memo (FINANCE, CASHIER, WAREHOUSE, or OWNER)", example = "OWNER")
    private String role;
}
