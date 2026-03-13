package lumi.insert.app.controller.transaction;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.service.PdfService;
import lumi.insert.app.service.TransactionService;
import lumi.insert.app.utils.mapper.AllTransactionMapper;

@SpringBootTest 
@WithMockUser(username = "admin", roles = {"ADMIN"})
@ActiveProfiles("test")
public abstract class BaseTransactionControllerTest {
     
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @Autowired
    AllTransactionMapper mapper;

    @MockitoBean
    PdfService pdfService;

    @BeforeEach
    void setup(WebApplicationContext context) {
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity()) 
            .build();
    }

    public TransactionResponse transactionResponse = new TransactionResponse(UUID.randomUUID(), "INVOICE", UUID.randomUUID(), null, 1L, null, null, null, null, null, null, null, null, null, null, null, null, null);
}
