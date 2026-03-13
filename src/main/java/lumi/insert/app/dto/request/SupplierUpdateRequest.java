package lumi.insert.app.dto.request; 
import jakarta.validation.constraints.Email; 
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing supplier")
public class SupplierUpdateRequest{
     
    @Pattern(regexp = "^[a-zA-Z0-9.-_ ]{5,30}$", message = "name has to be 5-30 length")
    @Schema(description = "Updated supplier name", example = "ABC Supply Co")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    @Schema(description = "Updated supplier email", example = "support@supplier.com")
    private String email;
 
    @Schema(description = "Updated supplier contact number", example = "081234567890")
    private String contact;

    @Schema(description = "Updated supplier active status", example = "true")
    private Boolean isActive;

}
