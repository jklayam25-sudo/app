package lumi.insert.app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductName {
        
    private Long id;

    private String name;
    
}
