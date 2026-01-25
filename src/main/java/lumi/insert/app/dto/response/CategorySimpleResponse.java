package lumi.insert.app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategorySimpleResponse {

    private Long id;

    private String name;

}