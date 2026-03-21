package lumi.insert.app.controller.employee;

import java.time.LocalDateTime;
import java.util.UUID;

import lumi.insert.app.controller.BaseControllerTest;
import lumi.insert.app.core.entity.nondatabase.EmployeeRole;
import lumi.insert.app.dto.response.EmployeeResponse; 

public abstract class BaseEmployeeControllerTest extends BaseControllerTest {
      
    EmployeeResponse employeeResponse = new EmployeeResponse(UUID.randomUUID(), "employeeU", "employeeF", EmployeeRole.CASHIER, LocalDateTime.now());
 
}
