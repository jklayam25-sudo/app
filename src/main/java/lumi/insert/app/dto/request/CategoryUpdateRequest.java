package lumi.insert.app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryUpdateRequest { 

    @NotNull
    private Long id;

    private String name;
    
}
