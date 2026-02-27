package lumi.insert.app.entity.nondatabase;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeLogin {
    
    private UUID id;

    private String username;

    private EmployeeRole role;
}
