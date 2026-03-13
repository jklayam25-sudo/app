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
@Schema(description = "Request to create a new customer")
public class CustomerCreateRequest {
    
    @NotBlank(message = "name cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9.-_ ]{5,30}$", message = "name has to be 5-30 length")
    @Schema(description = "Customer name", example = "John Smith")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    @Schema(description = "Customer email address", example = "john@example.com")
    private String email;

    @NotBlank(message = "shippingAddress cannot be empty")
    @Schema(description = "Contact information", example = "+1-555-0123")
    private String contact;

    @NotBlank(message = "shippingAddress cannot be empty")
    @Schema(description = "Shipping address", example = "123 Main St, City, State")
    private String shippingAddress;

}
