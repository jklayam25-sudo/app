package lumi.insert.app.dto.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class PaginationRequest {
    
    private Integer page;

    private Integer size;
    
}
