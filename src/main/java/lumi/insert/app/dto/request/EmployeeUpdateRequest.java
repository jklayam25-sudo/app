package lumi.insert.app.dto.request;
 
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lumi.insert.app.entity.nondatabase.EmployeeRole;

@Data
@Builder
public class EmployeeUpdateRequest {
     
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    private String username;
 
    private String fullname;
 
    private boolean isActive;
 
    private EmployeeRole role;
}
