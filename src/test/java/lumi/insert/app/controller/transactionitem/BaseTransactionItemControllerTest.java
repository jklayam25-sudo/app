package lumi.insert.app.controller.transactionitem;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import lumi.insert.app.dto.response.TransactionItemResponse;
import lumi.insert.app.service.TransactionItemService;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseTransactionItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TransactionItemService transactionItemService;

    public TransactionItemResponse transactionItemResponse = new TransactionItemResponse(UUID.randomUUID(), UUID.randomUUID(), 1L, null, 10L, 5L, null, null);
}