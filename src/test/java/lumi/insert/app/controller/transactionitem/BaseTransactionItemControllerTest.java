package lumi.insert.app.controller.transactionitem;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach; 
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.service.TransactionItemService;

@SpringBootTest 
@WithMockUser(username = "admin", roles = {"ADMIN"})
public abstract class BaseTransactionItemControllerTest {
 
    MockMvc mockMvc;

    @MockitoBean
    TransactionItemService transactionItemService;

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

    public TransactionItemResponse transactionItemResponse = new TransactionItemResponse(UUID.randomUUID(), UUID.randomUUID(), 1L, null, 10L, 5L, null, null);
}