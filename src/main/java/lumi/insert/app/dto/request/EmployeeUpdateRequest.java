package lumi.insert.app.dto.request;
  
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request to update an existing employee")
public class EmployeeUpdateRequest {
     
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    @Schema(description = "Updated username", example = "johndoe")
    private String username;
  
    @Pattern(regexp = "^[a-zA-Z ]{3,40}$", message = "Fullname has to be 3-40 length and doesn't contain unique char or number")
    @Schema(description = "Updated full name", example = "John Doe")
    private String fullname;
 
    @Schema(description = "Active status", example = "true")
    private Boolean isActive;
 
    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE", message = "check documentation for role specification")
    @Schema(description = "User role (FINANCE, CASHIER, or WAREHOUSE)", example = "CASHIER")
    private String role;
}
