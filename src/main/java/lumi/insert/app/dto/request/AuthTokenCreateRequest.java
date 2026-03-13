package lumi.insert.app.dto.request;
 
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "Request for user authentication/login")
public class AuthTokenCreateRequest {
    
    @NotBlank(message = "username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,30}$", message = "Username has to be 5-30 length and doesn't contain unique char")
    @Schema(description = "User username", example = "johndoe")
    private String username;

    @NotBlank(message = "password cannot be empty")
    @Pattern(regexp = "^(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{5,50}$", message = "password has to be 5-50 length and has atleast 1 unique char")
    @Schema(description = "User password", example = "Pass@123")
    private String password;
}
