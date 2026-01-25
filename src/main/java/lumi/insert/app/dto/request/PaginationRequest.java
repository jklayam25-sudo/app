package lumi.insert.app.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationRequest {
    
    private Integer page;

    private Integer size;
    
}
