package lumi.insert.app.dto.request;
 
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenCreateRequest {
    
    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,50}$", message = "password has to be 5-50 length and has atleast 1 unique char")
    private String password;
}
