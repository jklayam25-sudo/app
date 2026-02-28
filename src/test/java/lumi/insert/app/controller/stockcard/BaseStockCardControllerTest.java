package lumi.insert.app.controller.stockcard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.StockCardResponse;
import lumi.insert.app.entity.nondatabase.EmployeeLogin;
import lumi.insert.app.entity.nondatabase.EmployeeRole;
import lumi.insert.app.entity.nondatabase.StockMove;
import lumi.insert.app.service.implement.StockCardServiceImpl;

@SpringBootTest
@WithMockUser(username = "admin", authorities = {"ADMIN"})  
public abstract class BaseStockCardControllerTest {
    
    MockMvc mockMvc;

    @MockitoBean
    StockCardServiceImpl stockCardService;

    StockCardResponse stockCardResponse = new StockCardResponse(UUID.randomUUID(), UUID.randomUUID(), 1L, "Product", -5L, 10L, 5L, 1000L, StockMove.CUSTOMER_OUT, null, LocalDateTime.now());

    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("OWNER");

    EmployeeLogin employeeLogin = EmployeeLogin.builder()
        .id(UUID.randomUUID())
        .username("lumi")
        .role(EmployeeRole.OWNER)
        .build();

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(employeeLogin, null, authorities);

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

}
