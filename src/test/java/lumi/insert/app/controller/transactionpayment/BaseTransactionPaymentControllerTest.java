package lumi.insert.app.controller.transactionpayment;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
 
import lumi.insert.app.dto.response.TransactionPaymentResponse;
import lumi.insert.app.service.TransactionPaymentService;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseTransactionPaymentControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionPaymentService transactionPaymentService;

    TransactionPaymentResponse transactionPaymentResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "CLIENT", "LUMI", false);

    TransactionPaymentResponse transactionRefundResponse = new TransactionPaymentResponse(UUID.randomUUID(), UUID.randomUUID(), 10000L, "LUMI", "CLIENT", true);
}
