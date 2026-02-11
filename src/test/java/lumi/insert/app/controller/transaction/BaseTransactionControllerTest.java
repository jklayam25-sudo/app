package lumi.insert.app.controller.transaction;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.service.TransactionService;
import lumi.insert.app.utils.mapper.AllTransactionMapper;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseTransactionControllerTest {
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionService transactionService;

    @Autowired
    AllTransactionMapper mapper;

    public TransactionResponse transactionResponse = new TransactionResponse(UUID.randomUUID(), "INVOICE", UUID.randomUUID(), 1L, null, null, null, null, null, null, null, null, null, null, null, null, null);
}
