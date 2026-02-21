package lumi.insert.app.controller.authtoken;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.AuthTokenResponse;
import lumi.insert.app.entity.Employee;
import lumi.insert.app.service.AuthTokenService; 

@SpringBootTest
@WithMockUser(username = "admin", roles = {"ADMIN"})
public abstract class BaseAuthTokenControllerTest {
    

    MockMvc mockMvc;

    @MockitoBean
    AuthTokenService authTokenService;
    
    Employee setupEmployee = Employee.builder()
    .username("testEmployee")
    .build();

    AuthTokenResponse authTokenResponse = new AuthTokenResponse("someAccessToken", "someRefreshToken", null, LocalDateTime.now().plusDays(7), LocalDateTime.now());

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

}
