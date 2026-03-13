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
@Schema(description = "Request to update an existing customer")
public class CustomerUpdateRequest{
     
    @Pattern(regexp = "^[a-zA-Z0-9.-_ ]{5,30}$", message = "name has to be 5-30 length")
    @Schema(description = "Updated customer name", example = "John Smith")
    private String name;
 
    @Email(message = "email doesn't meet the email format")
    @Schema(description = "Updated email address", example = "john@example.com")
    private String email;
 
    @Schema(description = "Updated contact information", example = "+1-555-0123")
    private String contact;
 
    @Schema(description = "Updated shipping address", example = "123 Main St, City, State")
    private String shippingAddress;

    @Schema(description = "Active status", example = "true")
    private Boolean isActive;

    @Schema(description = "Latitude coordinate", example = "-6.2088")
    private Double Latitude;

    @Schema(description = "Longitude coordinate", example = "106.845")
    private Double Longitude;

}
