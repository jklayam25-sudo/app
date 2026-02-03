package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotBlank; 
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateRequest {

    @NotBlank(message = "Name cannot be empty")
    private String name;
    
}
