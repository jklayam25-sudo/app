package lumi.insert.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new supplier")
public class SupplierCreateRequest {
    
    @NotBlank(message = "name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9.-_ ]{5,30}$", message = "name has to be 5-30 length")
    @Schema(description = "Supplier name", example = "ABC Supplies Inc")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    @Schema(description = "Supplier email address", example = "contact@abcsupplies.com")
    private String email;

    @NotBlank(message = "shippingAddress cannot be empty")
    @Schema(description = "Supplier contact information", example = "+1-800-SUPPLIES")
    private String contact;

}
