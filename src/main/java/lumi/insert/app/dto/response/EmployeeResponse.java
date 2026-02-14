package lumi.insert.app.dto.response;

import java.util.UUID;

import lumi.insert.app.entity.nondatabase.EmployeeRole;

public record EmployeeResponse(UUID id, String username, String fullname, EmployeeRole role) {
    
}
