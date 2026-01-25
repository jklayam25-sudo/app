package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCreateRequest {

    @NotNull
    private String name;
    
}
