package lumi.insert.app.dto.request;
  
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data; 

@Data
@Builder
public class EmployeeUpdateRequest {
     
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    private String username;
  
    @Pattern(regexp = "^[a-zA-Z ]{3,40}$", message = "Fullname has to be 3-40 length and doesn't contain unique char or number")
    private String fullname;
 
    private Boolean isActive;
 
    @Pattern(regexp = "FINANCE|CASHIER|WAREHOUSE", message = "check documentation for role specification")
    private String role;
}
