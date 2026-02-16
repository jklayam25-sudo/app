package lumi.insert.app.controller.transactionpayment;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach; 
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.TransactionPaymentResponse;
import lumi.insert.app.service.TransactionPaymentService;

@SpringBootTest 
@WithMockUser(username = "admin", roles = {"ADMIN"})
public abstract class BaseTransactionPaymentControllerTest {
     
    MockMvc mockMvc;

    @MockitoBean
    TransactionPaymentService transactionPaymentService;

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

    TransactionPaymentResponse transactionPaymentResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "CLIENT", "LUMI", false);

    TransactionPaymentResponse transactionRefundResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "LUMI", "CLIENT", true);

}
