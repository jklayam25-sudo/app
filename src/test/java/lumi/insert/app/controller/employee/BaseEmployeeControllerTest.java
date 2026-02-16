package lumi.insert.app.controller.employee;

import java.util.UUID;
 
import org.junit.jupiter.api.BeforeEach; 
import org.springframework.boot.test.context.SpringBootTest; 
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.EmployeeResponse;
import lumi.insert.app.entity.nondatabase.EmployeeRole;
import lumi.insert.app.service.EmployeeService;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})
public abstract class BaseEmployeeControllerTest {
     
    MockMvc mockMvc;

    @MockitoBean
    EmployeeService employeeService;

    EmployeeResponse employeeResponse = new EmployeeResponse(UUID.randomUUID(), "employeeU", "employeeF", EmployeeRole.CASHIER);

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }
    
}
