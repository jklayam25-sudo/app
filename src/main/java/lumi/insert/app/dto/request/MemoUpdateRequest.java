package lumi.insert.app.dto.request;
 

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemoUpdateRequest { 
    String title;
 
    String body;
}
